#!/bin/bash
#
# Stops the hive server
#

source /etc/rtwsrc

SERVER_PID="$(jps | grep RunJar | awk '{print $1}')"
echo "Stopping $SERVER_PID"
kill -9 $SERVER_PID
rm -f /usr/local/rtws/ingest/logs/hiveserver.pid