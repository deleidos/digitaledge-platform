#!/bin/bash

# resolves the configured number of active zookeeper quorum servers for cluster/client usage

source /etc/rtwsrc

WAIT_FOREVER=$1

# Determine the number of zookeeper server's available
MAX_ZOOKEEPER_INSTANCES=500
ATTEMPT=1

if [ ! -z $WAIT_FOREVER ]; then
	echo "Chosing to wait \"forever\" for a zookeeper quorum to be established...."
	RETRIES=1000
else
	RETRIES=5
fi

while (( $ATTEMPT != $RETRIES ))
do
	RTWS_ZOOKEEPER_INSTANCE_TOTAL="$(java -cp /usr/local/rtws/commons-core/lib/saic-rtws-commons-core.jar: com.saic.rtws.commons.util.QueryJmxByProcessGroup "master.$RTWS_DOMAIN:1099" "rtws.saic.com:type=ProcessGroup,group=zookeeper" "ActiveProcessCount" | tr -d [:alpha:] | tr -d [:space:])"

	if [[ $? -eq 0 && ! -z $RTWS_ZOOKEEPER_INSTANCE_TOTAL ]]; then
		echo "Determined that there are $RTWS_ZOOKEEPER_INSTANCE_TOTAL zookeeper instances running, so updating the config as such."
		break 2;
	else
		echo "WARN: Unable to determine the zookeeper process group information."
	fi
	
	sleep 30	
	(( ATTEMPT += 1 ))
done

if [ -z $RTWS_ZOOKEEPER_INSTANCE_TOTAL ]; then
	echo "ERROR: There was an error fetching the zookeeper process group information.  The zookeeper config will not be set properly."
else
	
	# Protect against bad data
	if [ $RTWS_ZOOKEEPER_INSTANCE_TOTAL -gt $MAX_ZOOKEEPER_INSTANCES ]; then
		echo "WARN: Not using the supplied # of instances ($RTWS_ZOOKEEPER_INSTANCE_TOTAL), defaulting to 1."
		RTWS_ZOOKEEPER_INSTANCE_TOTAL=1
	fi
	
	x=1
	while (( x <= $RTWS_ZOOKEEPER_INSTANCE_TOTAL ))
	do
		RTWS_ZOOKEEPER_QUORUM_SERVERS="zookeeper${x}.$RTWS_DOMAIN,$RTWS_ZOOKEEPER_QUORUM_SERVERS"
		(( x += 1 ))
	done
	
	# Remove the trailing ',' from the list of servers (for Accumulo "$@$#@AA#$" ness with it...)
	# % is bash's way of triming characters which match the pattern
	TRIMED_QUORUM_SERVERS="${RTWS_ZOOKEEPER_QUORUM_SERVERS%?}"
	echo "RTWS_ZOOKEEPER_QUORUM_SERVERS=$TRIMED_QUORUM_SERVERS" >> /etc/rtwsrc
fi


# Update the host zoo.cfg if not going to be touched by normal zookeeper setup script
if [ -z "$(cat $RTWS_MANIFEST | grep setup_zookeeper_v2.sh)" ]; then
	echo "Updating default zoo.cfg"
	
	x=1
	for srv in $(echo $RTWS_ZOOKEEPER_QUORUM_SERVERS | tr -s ',' ' ')
	do
		echo "server.${x}=${srv}:2888:3888" >> /etc/zookeeper/conf/zoo.cfg
		(( x += 1 ))
	done
fi