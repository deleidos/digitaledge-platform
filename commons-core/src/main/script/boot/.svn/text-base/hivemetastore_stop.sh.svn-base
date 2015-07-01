#!/bin/bash
#
# Stops the metastore
#

source /etc/rtwsrc

HWI_PID="$(ps -ef | grep hwi | grep -v grep | grep hive | awk '{print $2}')"
echo "Stopping $HWI_PID"
kill -9 $HWI_PID

METASTORE_PID="$(ps -ef | grep metastore | grep -v grep | grep service | grep hive | awk '{print $2}')"
echo "Stopping $METASTORE_PID"
kill -9 $METASTORE_PID