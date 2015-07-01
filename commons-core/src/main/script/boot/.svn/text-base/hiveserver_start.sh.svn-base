#!/bin/bash
#
# Starts the hiveserver
#

source /etc/rtwsrc

# Only run the server the data sink needs it
# TODO Revisit if the simple check for "hive" in the config name
#        is enough to determine if the datasink needs hive
if [ ! -z "$(echo $RTWS_INGEST_CONFIG | grep hive)" ]; then
	hive --service hiveserver > /var/log/hive/hiveserver.log 2>&1 &
	hivePid=$!
    echo "Hive Server Pid: $hivePid"
    if [ ! -d /usr/local/rtws/ingest/logs ]; then
       mkdir /usr/local/rtws/ingest/logs
    fi
    echo $hivePid > /usr/local/rtws/ingest/logs/hiveserver.pid
	
fi