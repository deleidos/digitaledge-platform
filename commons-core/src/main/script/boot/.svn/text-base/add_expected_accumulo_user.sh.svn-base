#!/bin/bash
set -x

# Adds the specified user to the Accumulo cluster

source /etc/rtwsrc

sysctl -w vm.swappiness=0

if [ "$(whoami)" != "root" ]; then
     echo "ERROR: You must be root to proceed..."
    exit 1
fi

CONFIG_PREFIX="$(echo $RTWS_INGEST_CONFIG | sed "s/\.xml//g" | sed "s/services.//g" )"

RTWS_INGEST_CONFIG_DEFAULT="/usr/local/rtws/ingest/conf/pipeline.$CONFIG_PREFIX.default.xml"
PROCEED=1
if [ -f $RTWS_INGEST_CONFIG_DEFAULT ]; then
	
	ZOOKEEPER_REFERENCE="$(cat $RTWS_INGEST_CONFIG_DEFAULT | awk -F'<zookeeper-quorum>' '{print $2}' | awk -F'</zookeeper-quorum>' '{print $1}' | tr -sd '\n' '')"

	if [ ! -z $ZOOKEEPER_REFERENCE ]; then		
		echo "Datasink Zookeeper Quorum Property: ($ZOOKEEPER_REFERENCE)."
		# Grab user configured params needed by accumulo instnace
		INSTANCE_NAME="$(cat $RTWS_INGEST_CONFIG_DEFAULT | awk -F'<instance-name>' '{print $2}' | awk -F'</instance-name>' '{print $1}' | tr -sd '\n' '')"
		INSTANCE_USERNAME="$(cat $RTWS_INGEST_CONFIG_DEFAULT | awk -F'<username>' '{print $2}' | awk -F'</username>' '{print $1}' | tr -sd '\n' '')"
		INSTANCE_PASSWORD="$(cat $RTWS_INGEST_CONFIG_DEFAULT | awk -F'<password>' '{print $2}' | awk -F'</password>' '{print $1}' | tr -sd '\n' '')"
		PROCEED=0
	fi
fi

if [ $PROCEED -eq 0 ]; then
	
	# Wait until cluster is initialized
while (true)
do
	# TODO If the implementation changes, this may not work as a valid check to see if init was run
	hadoop fs -test -d /accumulo/instance_id
	if [ $? -eq 0 ]; then
		echo "Accumulo has been initialized, creating datasink expected user...."
		sleep 1m
		break 2;
	fi
	sleep 1m
done
	
	echo -e "changeme\nusers\n" | /usr/lib/accumulo-1.4.1/bin/accumulo shell -u root | grep -w $INSTANCE_USERNAME
	if [ $? -ne 0 ]; then
		cd /usr/lib/accumulo-1.4.1/ ; echo -e "changeme\ncreateuser $INSTANCE_USERNAME\n$INSTANCE_PASSWORD\n$INSTANCE_PASSWORD\ngrant  System.CREATE_TABLE -s -u $INSTANCE_USERNAME\n" | bin/accumulo shell
	fi
fi