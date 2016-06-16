#!/bin/bash
# Notes to launch a jenkins slave
# Copy key to /tmp on build machine
# Launch N instances of:
#	ami-714ba518 (ubuntu small)
#	ami-75c2031c (centos small)

## If lsb-release file is present, assume Ubuntu
if [[ -f /etc/lsb-release ]]; then
	
# Update each instance's config
	add-apt-repository "deb http://archive.canonical.com/ lucid partner"
	apt-get -y update
	apt-get -y install python-software-properties tofrodos g++ \
	pkg-config build-essential libfuse-dev fuse-utils libcurl4-openssl-dev libxml2-dev mime-support pkg-config unzip graphviz
fi

if [[ -f /etc/redhat-release ]]; then
	yum -y remove gcc-java gcc-gfortran gcc-gnat gcc-objc++ gcc-c++ cpp
        yum -y update
        yum -y upgrade
        yum -y install sudo dos2unix gcc-c++ libstdc++-devel \
        	fuse fuse-devel fuse-libs curl-devel libcurl-devel curl \
        	libxml2-devel openssl-devel mailcap \
        	xfsprogs mdadm bind yum-utils \
        	nfs-utils ant-antlr krb5-devel libgssglue libxml2
fi


# install s3fs
mkdir /tmp/s3fs-build ; cd /tmp/s3fs-build ; \
		wget http://s3fs.googlecode.com/files/s3fs-r191-source.tar.gz; tar xzvf s3fs-r191-source.tar.gz; \
		rm -f s3fs-r191-source.tar.gz; cd s3fs;  make install; cd /tmp ;  rm -fr /tmp/s3fs-build
	
	#add mount point
if [[ -f /etc/lsb-release ]]; then
	echo "fuse" >> /etc/modules
	modprobe fuse
fi

mkdir /mnt/appfs
if [ -z "$(cat /etc/fstab | grep s3fs)" ]; then
	   echo 's3fs#rtws.appfs /mnt/appfs fuse allow_other,accessKeyId=redacted,secretAccessKey=redacted 0 0' >> /etc/fstab
fi
mount /mnt/appfs

# Setup profile.d script		
cat >>/etc/profile.d/ec2.sh<<"EOF"
export EC2_HOME=/usr/local/ec2-api-tools
export EC2_AMITOOL_HOME=/usr/local/ec2-ami-tools
export PATH=$JAVA_HOME/bin:$PATH:$EC2_HOME/bin:$EC2_AMITOOL_HOME/bin
export EC2_PRIVATE_KEY=/usr/local/pk-DOXG47XPWAROWNJBAB4WTRA4NAZAIN2L.pem
export EC2_CERT=/usr/local/cert-DOXG47XPWAROWNJBAB4WTRA4NAZAIN2L.pem
EOF

				
# Grab Sun jdk
if [ ! -z "$(uname -i | grep i)" ]; then
	JDK_DOWNLOAD_URL=http://download.oracle.com/otn-pub/java/jdk/6u25-b06/jdk-6u25-linux-i586-rpm.bin
else
	JDK_DOWNLOAD_URL=http://download.oracle.com/otn-pub/java/jdk/6u25-b06/jdk-6u25-linux-x64-rpm.bin
fi
	
if [[ -f /etc/lsb-release ]]; then
groupadd -g 1000 ubuntu
adduser -g 1000 -Gadm,dialout,cdrom,floppy,audio ubuntu

mkdir /tmp/jdk ; cd /tmp/jdk ; \
wget $JDK_DOWNLOAD_URL ; \
chmod +x $(basename jdk-*-rpm.bin) ; echo "\n" | ./$(basename jdk-*-rpm.bin) ; mv jdk1.6* /usr/share/.
echo "export JAVA_HOME=/usr/local/$(basename /usr/share/jdk-*)" >> /etc/profile.d/ec2.sh
fi

if [[ -f /etc/redhat-release ]]; then
		mkdir /tmp/jdk ; cd /tmp/jdk \ 
        wget $JDK_DOWNLOAD_URL ; chmod +x $(basename jdk-*-rpm.bin) ; \
        echo "\n" | ./$(basename jdk-*-rpm.bin) ; rm -rf /tmp/jdk 
        echo "export JAVA_HOME=/usr/java/default" >> /etc/profile.d/ec2.sh
fi

# install EC2 Tools, bind, etc...
cd /usr/local ; wget https://ec2-downloads.s3.amazonaws.com/ec2-api-tools.zip ; unzip ec2-api-tools.zip
	ln -sf $(basename ec2-api-tools*) ec2-api-tools
	wget http://s3.amazonaws.com/ec2-downloads/ec2-ami-tools.zip ; unzip ec2-ami-tools.zip
	ln -sf $(basename ec2-ami-tools*) ec2-ami-tools
	rm ec2-api-tools.zip ec2-ami-tools.zip
	
	chmod a+x /etc/profile.d/ec2.sh
	. /etc/profile.d/ec2.sh
	
	#add ebs mount point
	VOLUME_INFO="$(ec2-create-volume -s 100 -z us-east-1c)"
	VOLUME_ID="$(echo $VOLUME_INFO | awk '{print $2}')"
	sleep 1m
	x=0
	while (( x = 0 ))
	do
		if [ ! -z "$(ec2-describe-volumes $VOLUME_ID | grep -w available)" ]; then
			x=1
	else
			echo "Requested EBS Volume isn't ready yet..."
			sleep 2m
		fi
	done
	ec2-attach-volume $VOLUME_ID -i $(curl -s http://169.254.169.254/latest/meta-data/instance-id) -d /dev/sdf1
	sleep 1m
	x=0
	while (( x = 0 ))
	do
		if [ ! -z "$(ec2-describe-volumes $VOLUME_ID | grep -w attached)" ]; then
			x=1
	else
			echo "Requested EBS Volume isn't attached yet..."
			sleep 2m
		fi
	done
	
	# Mount point
	# This is for weird issue where requested attachment was sdf1, but actual attachment was xvdf1
	if [ -b /dev/sdf1 ]; then 
		MOUNT_POINT=/dev/sdf1
	elif [ -b /dev/xvdf1 ]; then
		MOUNT_POINT=/dev/xvdf1
	fi
	
	mkfs.ext3 $MOUNT_POINT
	mkdir /tmp/jenkins ; chown -R ubuntu:ubuntu /tmp/jenkins
	if [ -z "$(cat /etc/fstab | grep jenkins)" ]; then
	   echo '$MOUNT_POINT       /tmp/jenkins        ext3    defaults        0       0' >> /etc/fstab
	fi
	mount /tmp/jenkins
	
# Make sure the instance(s) have the following user data (newline sep)
#AWS_EIP=<ip>23.21.235.60
#JENKINS_ROLE=<role | master | slave>
	
	#cd /etc/init.d/ ; update-rc.d associate_eip.sh defaults 95 05

