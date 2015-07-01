#!/bin/bash

sudo umount /mnt/appfs ; ls /mnt/appfs

source /etc/rtwsrc

build_release=`cat $RTWS_MANIFEST | grep -m 1 /mnt/appfs/release | cut -d'/' -f5`


if [ "$RTWS_MOUNT_MODE" = "s3cmd" ]; then
	#sync models, services, scripts
	
	rm -rf /mnt/appfs/services
	rm -rf /mnt/appfs/scripts
	
	mkdir -p /mnt/appfs/models
	mkdir -p /mnt/appfs/scripts
	mkdir -p /mnt/appfs/services

	s3cmd -c /home/rtws/.s3cfg get -rf s3://$RTWS_MOUNT_DEVICE/$build_release/models/ /mnt/appfs/models/
	s3cmd -c /home/rtws/.s3cfg get -rf s3://$RTWS_MOUNT_DEVICE/$build_release/scripts/ /mnt/appfs/scripts/
	s3cmd -c /home/rtws/.s3cfg get -rf s3://$RTWS_MOUNT_DEVICE/$build_release/services/ /mnt/appfs/services/
else
	sudo -u rtws /usr/bin/s3fs $RTWS_MOUNT_DEVICE -o default_acl=public-read -o passwd_file=/home/rtws/.s3fskey -o allow_other /mnt/appfs		
	ls /mnt/appfs
fi

curl -v -X POST http://localhost/repository/rest/content/sync/all