#!/bin/bash
set -x

# Set's up a single instance mongo db server using 10gen repo

source /etc/rtwsrc

if [ ! -d /mnt/rdafs/mongodb ]; then
	mkdir -p /mnt/rdafs/mongodb ; chown mongodb:mongodb /mnt/rdafs/mongodb
fi

# Update the expected port if needed based on datasink config
CONFIG_PREFIX="$(echo $RTWS_INGEST_CONFIG | sed "s/\.xml//g" | sed "s/services.//g" )"

RTWS_INGEST_CONFIG_DEFAULT="/usr/local/rtws/ingest/conf/pipeline.$CONFIG_PREFIX.default.xml"

if [ -f $RTWS_INGEST_CONFIG_DEFAULT ]; then
	
	EXPECTED_PORT="$(cat $RTWS_INGEST_CONFIG_DEFAULT | awk -F'<mongo-server-port>' '{print $2}' | awk -F'</mongo-server-port>' '{print $1}' | tr -sd '\n' '')"

	if [ ! -z $EXPECTED_PORT ]; then		
		echo "Swaping the port mongo runs on based on datasink requested port ($EXPECTED_PORT)."
		perl -i -pe "s/^#port = 27017/port=$EXPECTED_PORT/g" /etc/mongodb.conf
	fi
fi

chmod a+x /etc/init.d/mongodb

