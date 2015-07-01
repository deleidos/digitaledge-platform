#!/bin/bash
set -x

# Sets up a apt mirror, seeded by the contents in the specified s3 bucket for apt-get installs of any software (mongo, CDH, Apache Bigtop, s3cmd, etc...)

source /etc/rtwsrc

if [[ "$(whoami)" != "root" ]]; then
     echo "ERROR: You must be root to proceed..."
     exit 1
fi

if [ -z "$RTWS_APT_MIRROR_ROOT_LOCATION" ]; then
	RTWS_APT_MIRROR_ROOT_LOCATION="/mnt/rdafs/mirror"
	echo "Defaulting RTWS_APT_MIRROR_ROOT_LOCATION to $RTWS_APT_MIRROR_ROOT_LOCATION"
fi

if [ ! -d $RTWS_APT_MIRROR_ROOT_LOCATION ]; then
	mkdir -pv $RTWS_APT_MIRROR_ROOT_LOCATION
fi

# TODO replace install of lighttpd when baked into ami
apt-get -y update
apt-get -y install apt-mirror lighttpd
rm -fv /var/www/index.lighttpd.html

# Increase the download speed of s3cmd
if [ -f /home/rtws/.s3cfg ]; then
	INCREASED_VALUE="66560"
	CUR_RECV_VALUE="$(grep recv_chunk /home/rtws/.s3cfg | awk -F'=' '{print $2}' | tr -d '[:blank:]')"
	CUR_SEND_VALUE="$(grep send_chunk /home/rtws/.s3cfg | awk -F'=' '{print $2}' | tr -d '[:blank:]')"
	
	if [ -z "$CUR_RECV_VALUE" ]; then
		echo "recv_chunk = $INCREASED_VALUE" >> /home/rtws/.s3cfg
	else
		perl -i -pe "s/recv_chunk = $CUR_RECV_VALUE/recv_chunk = $INCREASED_VALUE/g" /home/rtws/.s3cfg
	fi
	if [ -z "$CUR_SEND_VALUE" ]; then
		echo "send_chunk = $INCREASED_VALUE" >> /home/rtws/.s3cfg
	else
		perl -i -pe "s/send_chunk = $CUR_SEND_VALUE/send_chunk = $INCREASED_VALUE/g" /home/rtws/.s3cfg
	fi
fi