#!/bin/bash

################################################################
#This script is copyright 2012, Leidos, Inc.
#This script is currently developed by the Leidos employees who are part of 
#the Leidos DigitalEdge Platform Development Team <DigitalEdge@leidos.com> Developers 
#include Jim Cannaliato, Matthew W. Vahlberg, and others.
#
#License: GPLv2+
#
#    This program is free software; you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation; either version 2 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program; if not, write to the Free Software
#    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA.
#
#See /usr/share/common-licenses/GPL-2, or <http://www.gnu.org/copyleft/gpl.txt> for the 
#terms of the latest version of the GNU General Public License.
################################################################

#
# Expected AWS userdata parameters.
# RTWS_ACCESS_KEY - The AWS access key associated with the account that this instance runs under. 
# RTWS_SECRET_KEY - The AWS secret key associated with the account that this instance runs under. 
# RTWS_MOUNT_DEVICE - The S3 bucket where the source code baseline can be downloaded.
# RTWS_MANIFEST - The manifest file for this instance. 
# RTWS_TENANT_ID - The tenant name that matches the AWS keys.
# RTWS_BUCKET - the bucket to store the processes.xml file
# RTWS_SW_VERSION - the software version we are running
# RTWS_IS_NAT - if present the AMI is configured for PAT as a NAT


IPADDR=`ifconfig | grep Bcast | grep -v --invert-match 0.0.0.0 | awk {'print $2'} | cut -f2 -d ':' | grep -v 169.254`
echo $IPADDR > /etc/hostname
hostname -F /etc/hostname

#####
# Check if DNS can resolve our hostname; if not add it to /etc/hosts
# This is primarily needed for Eucalyptus, because DNS works on Amazon 
##
HN=`/bin/hostname`
IP=`echo $HN | sed 's/^[^0-9]*//' | sed 's/-/./g'`
if [[ "`/usr/bin/host $HN`" != *$IP* ]]; then
  echo "$IP	$HN" >> /etc/hosts
fi

#####
# Get the instance startup parameters.
##

count=1

# Loop to check for User Data
while [ $count -le 19 ]; do

   params=`curl -s -f http://169.254.169.254/latest/user-data`

   echo This is what is in User Data: $params

   if [ ! "$params" ]; then
      sleep 10
      count=$(($count + 1))
      echo Retrying User Data Check
   else
      echo Found User Data, continuing with instance start.
      count=25
   fi

done

if [ -z "$params" ]; then
  # If the instance was started without userdata parameters, then it is probably being
  # used by a developer to set up a new image, rather than being auto started as a
  # production instance. Exit gracefully and skip the bootstrap.
  exit 0
fi

# Save the startup parameters in an rc file so other shell scripts can source them
# as environment variables.
if [ ! -e /etc/rtwsrc ]; then
  firstboot="true"
fi

echo $params | tr ';' '\n' > /etc/rtwsrc


#####
# Make sure all the needed parameters were passed.
##
source /etc/rtwsrc
RTWS_SHUTDOWN="/etc/init.d/.rtws.stop"

if [ -z "$RTWS_ACCESS_KEY" ]; then
  echo Instance user data parameter \"accessKey\" is missing.
  exit 1
fi

if [ -z "$RTWS_SW_VERSION" ]; then
  echo Instance user data parameter \"software version\" is missing.
  exit 1
fi

if [ -z "$RTWS_SECRET_KEY" ]; then
  echo Instance user data parameter \"secretKey\" is missing.
  exit 1
fi

if [ -z "$RTWS_MOUNT_DEVICE" ]; then
  echo Instance user data parameter \"mount device\" is missing.
  exit 1
fi

if [ -z "$RTWS_BUCKET_NAME" ]; then
  echo Instance user data parameter \"bucket\" is missing.
  exit 1
fi

if [ -z "$RTWS_MANIFEST" ]; then
  echo Instance user data parameter \"manifest\" does not exist.
  exit 1
fi

if [ -z "$RTWS_DOMAIN" ]; then
  echo Instance user data parameter \"domain\" does not exist.
  exit 1
fi

if [ -z "$RTWS_STORAGE_ENDPOINT" ]; then
  echo Instance user data parameter \"storage endpoint\" does not exist.
  exit 1
fi


####
# Make the hostname the system's fqdn for better processing 
# by the log aggregation appliance
if [[ "$(echo $RTWS_MANIFEST | grep -cw master)" -eq 1 ]]; then
	h_fqdn="master.$RTWS_DOMAIN"
else
	h_fqdn="$RTWS_FQDN"
fi   	
   
echo "Changing the hostname to: $h_fqdn"
echo $h_fqdn > /etc/hostname
hostname -F /etc/hostname


echo RTWS_MOUNT_MODE = $RTWS_MOUNT_MODE
echo RTWS_MOUNT_DEVICE = $RTWS_MOUNT_DEVICE

#####
# Swap fstab entry if applicable
cat /proc/partitions | grep vda
FSTAB_CHECK_RTN=$?
if [ $FSTAB_CHECK_RTN -eq 0 ]; then
	echo "Updating /etc/fstab"
	for i in 2 3 4
	do
		perl -i -pe "s/sda${i}/vda${i}/g" /etc/fstab
	done
	
	mount -a
fi

#####
# Mount the s3 file system.
##
if [ "$RTWS_MOUNT_MODE" = "s3fs" -o "$RTWS_MOUNT_MODE" = "" ]; then
  i=1
  # try to mount it for two minutes (24 * 5 seconds)
  while [ $i -le 24 ]; do
    # mount check command
    mounted=`mount | grep s3fs | grep /mnt/appfs`
  
    if [ -n "$mounted" ]; then
      echo "Device $RTWS_MOUNT_DEVICE is mounted [$mounted]"
      break
    else
      echo "Device $RTWS_MOUNT_DEVICE not mounted on /mnt/appfs, sleeping for 5 seconds..."
      sleep 5
    fi
    
    if [ ! -e /mnt/appfs ]; then
      mkdir /mnt/appfs
      chmod 755 /mnt/appfs 
      chown rtws /mnt/appfs
      chgrp rtws /mnt/appfs
    fi
    
    
    if [ ! -e /home/rtws/.s3fskey ]; then
    	echo -n $RTWS_ACCESS_KEY > /home/rtws/.s3fskey
    	echo -n : >> /home/rtws/.s3fskey
    	echo $RTWS_SECRET_KEY >> /home/rtws/.s3fskey
    	chmod 600 /home/rtws/.s3fskey
    	chown rtws:rtws /home/rtws/.s3fskey
    	
    	#do once
    	echo user_allow_other > /etc/fuse.conf
    fi

	chmod 644 /etc/fuse.conf
	su rtws -c "/usr/bin/s3fs $RTWS_MOUNT_DEVICE -o default_acl=public-read -o passwd_file=/home/rtws/.s3fskey -o allow_other /mnt/appfs"
    #old /usr/bin/s3fs $RTWS_MOUNT_DEVICE -o default_acl=public-read -o accessKeyId=$RTWS_ACCESS_KEY -o secretAccessKey=$RTWS_SECRET_KEY -o allow_other /mnt/appfs
    
    # increment counter
    i=$(( $i + 1 ))
  done
elif [ "$RTWS_MOUNT_MODE" = "nfs" ]; then
  # sleep so that /mnt has time to fully initialize
  sleep 30
  # create directory if it doesn't exist
  if [ ! -e /mnt/appfs ]; then
    mkdir /mnt/appfs
  fi
  # mount the director
  mount $RTWS_MOUNT_DEVICE /mnt/appfs
elif [ "$RTWS_MOUNT_MODE" = "s3cmd" ]; then

	if [ ! -e /home/rtws/.s3cfg ]; then
            #general code only nothing specific to service provider
            cp /usr/local/rtws/boot/.s3cfg  /home/rtws/.s3cfg
            url=$RTWS_STORAGE_ENDPOINT

            scheme=$( echo "$url" | perl -MURI -le 'chomp($url = <>); print URI->new($url)->scheme' )
            host_port=$( echo "$url" | perl -MURI -le 'chomp($url = <>); print URI->new($url)->host_port' )
            path=$( echo "$url" | perl -MURI -le 'chomp($url = <>); print URI->new($url)->path' )

            echo host_base = $host_port >> /home/rtws/.s3cfg
            
            # On the hardened Ubuntu 12.04 image, having the host_bucket = s3.amazonaws.com
            # causes: ERROR: S3 error: 403 (SignatureDoesNotMatch): The request signature we calculated does not match the signature you provided. Check your key and signing method.
            # so don't add it.
            if [ -z "$(cat /etc/lsb-release | grep DISTRIB_CODENAME=precise)" ]; then
               echo host_bucket = $host_port >> /home/rtws/.s3cfg
            fi
            echo service_path = $path >> /home/rtws/.s3cfg

            if [[ "$scheme" == https ]]; then
                    echo use_https = True >> /home/rtws/.s3cfg
            else
                    echo use_https = False >> /home/rtws/.s3cfg
            fi

            echo access_key = $RTWS_ACCESS_KEY >> /home/rtws/.s3cfg
            echo secret_key = $RTWS_SECRET_KEY >> /home/rtws/.s3cfg
	fi
	
	echo Check if connected to endpoint...
	curlCommand=`curl --write-out %{http_code} --silent --output /dev/null $RTWS_STORAGE_ENDPOINT`
	
	# Euca 3.4.2+ requires authentication and will throw a 403, this check is
	# made just to make sure we can talk to Walrus, not access everything (AWS throws 307)
	while [ $curlCommand -ge 400 ] && [ $curlCommand != 403 ]
	do
       	echo Unable to access Walrus...
       	sleep 5
       	curlCommand=`curl --write-out %{http_code} --silent --output /dev/null $RTWS_STORAGE_ENDPOINT`
	done
	

	echo Using s3cmd to get resources
	#get code/etc via s3cmd
	
  	#some setup: copying the fast/fixed/easy things over...
  	mkdir -p /mnt/appfs/configuration/$RTWS_DOMAIN
  	mkdir -p /mnt/appfs/manifest/$RTWS_DOMAIN
	mkdir -p /mnt/appfs/scripts
	mkdir -p /mnt/appfs/services
	mkdir -p /mnt/appfs/release/$RTWS_SW_VERSION

	s3cmd -c /home/rtws/.s3cfg get -rf s3://$RTWS_MOUNT_DEVICE/$RTWS_SW_VERSION/configuration/$RTWS_DOMAIN/ /mnt/appfs/configuration/$RTWS_DOMAIN/
	s3cmd -c /home/rtws/.s3cfg get -rf s3://$RTWS_MOUNT_DEVICE/$RTWS_SW_VERSION/manifest/$RTWS_DOMAIN/ /mnt/appfs/manifest/$RTWS_DOMAIN/
	s3cmd -c /home/rtws/.s3cfg get -rf s3://$RTWS_MOUNT_DEVICE/$RTWS_SW_VERSION/scripts/ /mnt/appfs/scripts/
	s3cmd -c /home/rtws/.s3cfg get -rf s3://$RTWS_MOUNT_DEVICE/$RTWS_SW_VERSION/services/ /mnt/appfs/services/
  	
  	#change permissions
  	chown -R rtws /mnt/appfs
    chgrp -R rtws /mnt/appfs
  	
  	#release will get copied over as requested by manifest
	

else
  echo "Device /mnt/appfs was NOT mounted. Skipping mount. RTWS_MOUNT_MODE=$RTWS_MOUNT_MODE RTWS_MOUNT_DEVICE=$RTWS_MOUNT_DEVICE"
fi

#####
# Create rtws.properties file
##
if [ -n "$RTWS_PROPERTIES" ]; then
  if [ ! -e "/etc/rtws.properties" ]; then
    # Transfer key/value pairs from RTWS_PROPERTIES variable to rtws.properties file
    echo "# Auto-generated from User-Data RTWS_PROPERTIES" > /etc/rtws.properties
    for var in ${RTWS_PROPERTIES//&/ }; do
       #echo $var
       v1=${var#*=}
       v2=${v1//+/%20}
       v3="${var%=*}="
       #printf "%s=" ${var%=*}
       for part in ${v2//%/ \\x}; do
           printf -v v3 "%s%b%s" "${v3}" "${part:0:4}" "${part:4}"
       done
       echo "tenant.system.${v3}" >> /etc/rtws.properties
       #echo ${var%=*} $v2
    done
  fi
  
  # Create properties configuration files for system
  ant -f /usr/local/rtws/boot/rtws_build.xml build-system
fi


#####
# Check Manifest
##
if [ ! -e "$RTWS_MANIFEST" ]; then
  if [ -z "$RTWS_RELEASE" ]; then
    echo Manifest file does not exist: $RTWS_MANIFEST
    exit 1
  else
    echo Using template manifest, replacing properties:  $RTWS_MANIFEST
    #get manifest from templates and move to /usr/local/rtws/boot, then replace domain and release in manifest, then set new manifest env var
    if [ "$RTWS_MOUNT_MODE" = "s3cmd" ]; then
    	# Pre-create directory due to bug in s3cmd & boot time execution env
		mkdir /mnt/appfs/release/$RTWS_RELEASE
		s3cmd -c /home/rtws/.s3cfg -f get s3://$RTWS_MOUNT_DEVICE/$RTWS_RELEASE/release/deleidos-rtws-commons-config.tar.gz /mnt/appfs/release/$RTWS_RELEASE/
		
		#change permissions
  		chown -R rtws /mnt/appfs/release/$RTWS_RELEASE/
    	chgrp -R rtws /mnt/appfs/release/$RTWS_RELEASE/
		
    fi
    tar --directory=/usr/local/rtws/boot -xvzf /mnt/appfs/release/$RTWS_RELEASE/deleidos-rtws-commons-config.tar.gz commons-config/src/templates/manifest/$RTWS_MANIFEST
    cp /usr/local/rtws/boot/commons-config/src/templates/manifest/$RTWS_MANIFEST /usr/local/rtws/boot/
    rm -rf /usr/local/rtws/boot/commons-config

    perl /usr/local/rtws/boot/rtws_property_replace.pl /usr/local/rtws/boot/$RTWS_MANIFEST build.domain $RTWS_DOMAIN
    perl /usr/local/rtws/boot/rtws_property_replace.pl /usr/local/rtws/boot/$RTWS_MANIFEST build.release $RTWS_RELEASE

    export RTWS_MANIFEST=/usr/local/rtws/boot/$RTWS_MANIFEST
    echo RTWS_MANIFEST=$RTWS_MANIFEST
  fi
fi


#####
# Remove the shutdown script.
##
if [ -e "$RTWS_SHUTDOWN" ]; then
  rm -f $RTWS_SHUTDOWN
fi

#####
#If RTWS_IS_NAT is present configure ami to run as a nat.
##
if [ "$RTWS_IS_NAT" = "true" ]; then
	/usr/local/rtws/boot/rtws_configure_pat.sh
fi

#####
# Run the setup routines listed in the manifest.
##
cat $RTWS_MANIFEST | tr -d '\r' | while read line; do

  echo "$line" | tr '|' ' '
  comment=`echo "$line" | cut -c1`
  action=`echo "$line" | cut -d'|' -f1`
  
  # Skip comment lines.
  if [ "$comment" = "#" ]; then
  
    echo "Do Nothing" > /dev/null
    
  # Moves the specified directory to a new location.
  elif [ "$action" = "ARCHIVE" ]; then
  
    usr=`echo $line | cut -d'|' -f2`
    dir=`echo $line | cut -d'|' -f3`
    arc=`echo $line | cut -d'|' -f4`
    
    if [ -d "$dir" ]; then
      rm -fr $arc
      mv $dir $arc
      chown $usr $arc
    fi
    
  # Unpacks a tar file into a specified location.
    elif [ "$action" = "INSTALL" ]; then

    for retries in {1..5}
    do
        usr=`echo $line | cut -d'|' -f2`
        tar=`echo $line | cut -d'|' -f3`
        dir=`echo $line | cut -d'|' -f4`

        if [ "$RTWS_MOUNT_MODE" = "s3cmd" ]; then
                s3fil=`echo $tar | cut -d'/' -f 6-`
                s3dir=`echo $tar | cut -d'/' -f 4`
                echo $s3fil is s3file
                if [ "$s3dir" = "release" ]; then
                        s3src="s3://$RTWS_MOUNT_DEVICE/$RTWS_SW_VERSION/$s3dir/$s3fil"
                else
                        s3src="s3://$RTWS_MOUNT_DEVICE/$RTWS_SW_VERSION/$s3dir/$RTWS_DOMAIN/$s3fil"
                fi
                echo get $s3src
                echo put to $tar
                s3cmd -c /home/rtws/.s3cfg -f get $s3src $tar

                #change permissions
                chown -R rtws $tar
                chgrp -R rtws $tar

        fi

        # Unpack the latest archive
        cd $dir
        su $usr -p -c "tar -zxf $tar"

        if test $? -eq 0; then
                        break
                else
                        echo "Tar failed, retrying a $retries times"
                fi

    done
    
  # Run the requested ansible playbook PLAYBOOK|user|path to playbook's site.yml
  # E.g.) PLAYBOOK|root|/usr/local/rtws/playbooks/tms-jdbc-setup   
  elif [ "$action" = "PLAYBOOK" ]; then
    
  	usr=`echo $line | cut -d'|' -f2`
  	playbook_dir=`echo $line | cut -d'|' -f3`
  	
  	if [ -d "$playbook_dir" ]; then
  		su $usr -p -c "cd $playbook_dir ; ansible-playbook site.yml -c local"
    else 
    	echo "ERROR: $playbook_dir does not exist."
    fi
  
  # Remove a file given its absolute location  
  elif [ "$action" = "REMOVE" -o "$action" = "DELETE" ]; then
    
  	usr=`echo $line | cut -d'|' -f2`
  	file=`echo $line | cut -d'|' -f3`
  	
  	if [ -f "$file" ]; then
  		su $usr -p -c "rm $file"
    fi
    
  # Rename a file or directory from one name to another
  elif [ "$action" = "RENAME" ]; then
  
  	usr=`echo $line | cut -d'|' -f2`
  	from=`echo $line | cut -d'|' -f3`
  	to=`echo $line | cut -d'|' -f4`
    
    if [ -f "$from" -o -d "$from" ]; then
  	  su $usr -p -c "mv $from $to"
    fi
    
  # Make a new directory
  elif [ "$action" = "MKDIR" ]; then
  
  	usr=`echo $line | cut -d'|' -f2`
  	dir=`echo $line | cut -d'|' -f3`
    
    if [ ! -d "$dir" ]; then
  	  su $usr -p -c "mkdir -p $dir"
    fi
    
  # Recursively changes permissions on file that match a specified pattern.
  elif [ "$action" = "CHMOD" ]; then
  
    rwx=`echo $line | cut -d'|' -f2`
    dir=`echo $line | cut -d'|' -f3`
    pat=`echo $line | cut -d'|' -f4`
  
    find $dir -name "$pat" -exec chmod $rwx {} \;
    
  # Recursively converts DOS files to UNIX line feed format.
  elif [ "$action" = "FIXLF" ]; then
  
    dir=`echo $line | cut -d'|' -f2`
    pat=`echo $line | cut -d'|' -f3`
  
	# Run the right dos2unix command for platform (Ubuntu vs Centos)
	if [ -f /etc/lsb-release ]; then
     find $dir -name "$pat" -exec fromdos -p {} \;
	else
		find $dir -name "$pat" -exec dos2unix {} \;
	fi
    
  # Runs a specified script.
  elif [ "$action" = "START" ]; then
  
    usr=`echo $line | cut -d'|' -f2`
    cmd=`echo $line | cut -d'|' -f3`
    dir=`echo $cmd | cut -d' ' -f1`
    dir=`dirname $dir`
    
    cd $dir
    su $usr -p -c "$cmd"
    
    
  elif [ "$action" = "S3GET" ]; then
  
    usr=`echo $line | cut -d'|' -f2`
    file=`echo $line | cut -d'|' -f3`
    dest=`echo $line | cut -d'|' -f4`
    
    if [ ! -f $dest/$file ]; then
    	s3cmd -c /home/rtws/.s3cfg -f get s3://$RTWS_MOUNT_DEVICE/$RTWS_SW_VERSION/$file $dest
    	
    	su $usr -p -c "$cmd"
    fi 
    
    
  # Runs a specified script (but only the first time the instance starts).
  elif [ "$action" = "SETUP" ]; then
  
    if [ "$firstboot" = "true" ]; then
    
      usr=`echo $line | cut -d'|' -f2`
      cmd=`echo $line | cut -d'|' -f3`
      dir=`echo $cmd | cut -d' ' -f1`
      dir=`dirname $dir`
      
      cd $dir
      su $usr -p -c "$cmd"
      
    fi
    
  # Schedules the specified script to be run during shutdown.
  elif [ "$action" = "STOP" ]; then
  
    echo $line >> $RTWS_SHUTDOWN
    
  # Make a symbolic link
  elif [ "$action" = "LINK" ]; then
    
    usr=`echo $line | cut -d'|' -f2`
    target=`echo $line | cut -d'|' -f3`
    name=`echo $line | cut -d'|' -f4`
    
    su $usr -p -c "rm -f $name"
    su $usr -p -c "ln -s $target $name"
    
  fi
  
done

