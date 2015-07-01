#!/bin/bash
set -x

# Updates the internal mirror from contents in s3

source /etc/rtwsrc


if [ -z "$RTWS_APT_MIRROR_ROOT_LOCATION" ]; then
	RTWS_APT_MIRROR_ROOT_LOCATION="/mnt/rdafs/"
	echo "Defaulting RTWS_APT_MIRROR_ROOT_LOCATION to $RTWS_APT_MIRROR_ROOT_LOCATION"
fi

if [ ! -d $RTWS_APT_MIRROR_ROOT_LOCATION ]; then
	mkdir -pv $RTWS_APT_MIRROR_ROOT_LOCATION
fi


if [[ "$(whoami)" != "root" ]]; then
     echo "ERROR: You must be root to proceed..."
     exit 1
fi

if [ -z "$RTWS_MOUNT_DEVICE" ]; then
	  echo "ERROR: RTWS_MOUNT_DEVICE not defined..."
     exit 1
fi

s3cmd -c /home/rtws/.s3cfg -rf get s3://$RTWS_MOUNT_DEVICE/mirror $RTWS_APT_MIRROR_ROOT_LOCATION

# Link mirrored domain content excluding their domain prefix
cd $RTWS_APT_MIRROR_ROOT_LOCATION/mirror/
for domain in $(/bin/ls -d *)
do
	cd $domain
	for dir in $(/bin/ls -d *)
	do
		ln -sf $RTWS_APT_MIRROR_ROOT_LOCATION/mirror/$domain/$dir /var/www/.
	done
	cd -
done