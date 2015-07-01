#!/bin/bash
set -x

source /etc/rtwsrc

# Sets up a mirror of the CDH4 release for offline auto installs

if [ -z "$RTWS_APT_MIRROR_ROOT_LOCATION" ]; then
	RTWS_APT_MIRROR_ROOT_LOCATION="/mnt/rdafs/mirror"
	echo "Defaulting RTWS_APT_MIRROR_ROOT_LOCATION to $RTWS_APT_MIRROR_ROOT_LOCATION"
fi

if [ ! -d $RTWS_APT_MIRROR_ROOT_LOCATION ]; then
	mkdir -pv $RTWS_APT_MIRROR_ROOT_LOCATION
fi


cd $RTWS_APT_MIRROR_ROOT_LOCATION

apt-key adv --keyserver keyserver.ubuntu.com --recv 7F0CEB10

wget -rc http://downloads-distro.mongodb.org/repo/ubuntu-upstart

cd $RTWS_APT_MIRROR_ROOT_LOCATION
s3cmd -c /home/rtws/.s3cfg -rf put . s3://$RTWS_MOUNT_DEVICE/mirror/

echo "Please grant full controll to tms on s3://$RTWS_MOUNT_DEVICE/mirror using your favorite tool  (AWS or Euca)".
