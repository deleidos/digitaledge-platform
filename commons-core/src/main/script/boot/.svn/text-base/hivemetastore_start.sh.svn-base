#!/bin/bash
#
# Starts the metastore & web UI
#

source /etc/rtwsrc

hive --service metastore > /var/log/hive/hivemetastore.log 2>&1 &

if [ -z $ANT_LIB ]; then
	export ANT_LIB=/usr/share/ant/lib/
fi

hive --service hwi > /var/log/hive/hivewebui.log 2>&1 &