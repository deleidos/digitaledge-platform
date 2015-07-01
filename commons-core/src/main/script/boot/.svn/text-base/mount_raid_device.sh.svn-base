#!/bin/bash

#
# Expected AWS userdata parameters.
# RTWS_RAID_DEVICES - Comma separated list of device names that should be managed as a single volume.
# RTWS_RAID_PATH - The file system path on which to mount the RAID.
#
#
#  Note: Support for newer kernels added as mentioned in AWS console
#      "....Note: Newer linux kernels may rename your devices to /dev/xvdf through /dev/xvdp internally, 
#		  even when the device name entered here (and shown in the details) is /dev/sdf through /dev/sdp."

if [ $# -ne 3 ]; then
  echo "usage: mount_raid_device.sh [user] [group] [mode]"
  exit 1
fi
user=$1
group=$2
mode=$3

source /etc/rtwsrc


# Java hook here is to block until all volumes are attached.
# It's in java vs the ec2-XXX tools to avoid having to create and distribute a cert
java -classpath $(echo /usr/local/rtws/boot-apps/lib/*|tr -s ' ' ':'):/usr/local/rtws/commons-core/conf/ com.saic.rtws.commons.cloud.util.BootVolumesApp

if [ $? -ne 0 ]; then
	echo "ERROR: The blocking app had a problem.  ($?)"
fi


deviceNames=`echo $RTWS_RAID_DEVICES | tr ',' ' '`
deviceCount=`echo $deviceNames | wc -w`

# If there are no devices to be mounted, do nothing.
if [ $deviceCount -eq 0 ]; then
	
	# Always create base path, set initial permissions on root folder
	
	if [ ! -z $RTWS_RAID_PATH ]; then
	
		mkdir -p $RTWS_RAID_PATH
		chown $user:$group $RTWS_RAID_PATH
   	 	chmod $mode $RTWS_RAID_PATH
    
    fi

	exit 0
elif [ -z "$RTWS_RAID_PATH" ]; then
	echo "No mount point specified."
	exit 1
fi

# Wait until EC2 attaches the 1st of the requested devices (regardless of name)
unattachedDevices=`echo $RTWS_RAID_DEVICES | tr ',' ' ' | awk '{print $1}'`
while [ -n "$unattachedDevices" ]
do
	for device in $unattachedDevices
	do
		case $device in 
			/dev/sd[f-p][0-9] | /dev/xvd[f-p][0-9] | /dev/vd[b-p] )
					if [ -e $device ]; then
						unattachedDevices=`echo "$unattachedDevices" | tr ' ' '\n' | grep -v $device | xargs`
					fi
				;;
			* ) 
				echo "Unknown volume found...."
				exit 1				
				;;
		esac
	done
	
	if [ "$wt" == "...................................." ]; then
		echo "Expected storage devices not attached: $deviceNames"
		echo "Falling through to see if they were attached under an unexpected name."
		unattachedDevices=`echo "$unattachedDevices" | tr ' ' '\n' | grep -v $device | xargs`
	elif [ -n "$unattachedDevices" ]; then
		wt="$wt""."
		sleep 5
	fi
done

deviceNamesCheck=`grep RTWS_RAID_DEVICES /etc/rtwsrc | tr ',' ' ' | awk -F'=' '{print $2}' | sed "s/\/dev\///g" `
# Check and replace with correct mount points
deviceNamesReset=1
for device in $deviceNamesCheck 
do 
	cat /proc/partitions | grep -w $device > /dev/null 2>&1
	if [ $? -ne 0 ]; then   	
		# Look for the correct one
		for pre in sd xvd vd
		do
			for vname in b c d e f g h i j k l m n o p
			do
				num="$(echo $device | tr -d '[a-z]')"
				if [ -e /dev/${pre}${vname}${num} ]; then
					echo "WARNING: Correcting block device for $device for newer kernel"
					newName="${pre}${vname}"
            				perl -i -pe "s/$(echo $device | tr -d '[0-9]')/${newName}/g" /etc/rtwsrc
            		
					# Re-source and move on
					source /etc/rtwsrc
					deviceNames=`echo $RTWS_RAID_DEVICES | tr ',' ' '`
					deviceCount=`echo $deviceNames | wc -w`
            				break 3;
				elif [  -e /dev/${pre}${vname} ]; then
				        echo "WARNING: Correcting block device for $device for newer kernel"
                                        newName="${pre}${vname}"
                                        perl -i -pe "s/$(echo $device)/${newName}/g" /etc/rtwsrc

                                        # Re-source and move on
                                        source /etc/rtwsrc
                                        deviceNames=`echo $RTWS_RAID_DEVICES | tr ',' ' '`
                                        deviceCount=`echo $deviceNames | wc -w`
                                        break 3;
				fi				
			done
		done            
	fi
done

# Safety net to wait for all of the expected (possibly renamed) devices to be attached.
unattachedDevices=$deviceNames
while [ -n "$unattachedDevices" ]
do
	for device in $unattachedDevices
	do
		if [ -e $device ]; then
			unattachedDevices=`echo "$unattachedDevices" | tr ' ' '\n' | grep -v $device | xargs`
		fi
	done
	if [ "$wait" == "............" ]; then
		echo "Expected storage devices not attached: $deviceNames"
		exit 1
	elif [ -n "$unattachedDevices" ]; then
		wait="$wait""."
		sleep 10
	fi
done

# Set the mount device
if [ $deviceCount -gt 1 ]; then
    mountDevice=/dev/md0
else
    mountDevice=$deviceNames
fi 

# Java hook here is to check the creation time of all volumes.
# It's in java vs the ec2-XXX tools to avoid having to create and distribute a cert
java -classpath $(echo /usr/local/rtws/boot-apps/lib/*|tr -s ' ' ':'):/usr/local/rtws/commons-core/conf/ com.saic.rtws.commons.cloud.util.CheckVolumeCreationTime

canCreate=$?

if [ $canCreate -ne 0 ]; then
	echo "INFO: A volume was not within the creation window, no filesystem can be created on the volumes, existing volumes/raid.  ($canCreate)"
fi

# Loop until the device is mounted
while [ 1 ]; do

    mounted=`mount | grep $mountDevice`
    
    mountSleepTime=`cat /usr/local/rtws/properties/rtws-common.properties | grep eucalyptus.blades.attach.volumes.delay.time.seconds | cut -d'=' -f2`
    if [ -z "$mountSleepTime" ]; then
	mountSleepTime=5
    fi
    
    if [ -n "$mounted" ]; then
        echo "Device $mountDevice is mounted [$mounted], exiting..."
        break
    else
        echo "Device $mountDevice not mounted, sleeping for $mountSleepTime seconds..."
        sleep $mountSleepTime
    fi

    # If there is more than one device specified, build the RAID.
    if [ $deviceCount -gt 1 ]; then

		#checks to see if it can read the superblock on device, lists info, if info returned then reassemble devices
		formattedDevices=`mdadm --examine $deviceNames`
		#this is a better solution, but will not work with new AMI
		#formattedDevices=`mdadm --query --detail /dev/md0 | grep "dev" | awk '{if (NR!=1) {print}}' | awk '{print $7}'`
	
		if [ ! -z "$RTWS_FQDN" ]; then
	    	raidName="$(echo $RTWS_FQDN | cut -c1-31)"
	    elif [ ! -z "$RTWS_MANIFEST" ]; then
	    	# Grab from manifest path
	    	tmpName="$(echo $RTWS_MANIFEST | cut -d/ -f5)"
	    	raidName="$(echo master.$tmpName | cut -c1-31)"
	    else
	    	raidName="UNABLE_TO_DETERMINE_NAME"
	    fi
	    
	    # If the devices are already formatted, re-assemble the existing RAID.
	    if [ -n "$formattedDevices" ]; then
		    mdadm --assemble $mountDevice $deviceNames		
	    # Otherwise, build the RAID partitions.
	    else
	    	if [ $canCreate -eq 0 ]; then
		    	mdadm --create $mountDevice --name="$raidName" --chunk=256 --level=0 --metadata=1.1 --raid-devices=$deviceCount $deviceNames
		    	mkfs.xfs $mountDevice
		    else
		    	echo "ERROR: A volume was not within the creation window, no filesystem can be created on the volumes, existing volumes/raid.  ($canCreate), exiting."
		    	exit 1
		    fi
	    fi
	
    # If there is only one device, it can simply be mounted directly.
    else

	    fileSystem=`file -s $mountDevice | grep "XFS"`
	
	    # If the device is blank; build a file system.
	    if [ -z "$fileSystem" ]; then
		    mkfs.xfs $mountDevice
	    fi
	
    fi

    # Set device properties/permissions and mount the volume.
    blockdev --setra 16384 $deviceNames
    
    if [ ! -d $RTWS_RAID_PATH ]; then
        mkdir $RTWS_RAID_PATH
    fi
    
    mount $mountDevice $RTWS_RAID_PATH
    chown $user:$group $RTWS_RAID_PATH
    chmod $mode $RTWS_RAID_PATH

done
