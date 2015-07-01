#!/bin/bash

if [[ "$(whoami)" != "root" ]]; then
     echo "ERROR: You must be root to proceed..."
     exit -1
fi

if [ -f /usr/local/apache-activemq/conf/log4j.properties ]; then
	echo "Tweaking activemq log retention settings"
	perl -i -pe 's/log4j.appender.logfile.maxBackupIndex=5/log4j.appender.logfile.maxBackupIndex=2/g' /usr/local/apache-activemq/conf/log4j.properties
	chown activemq:activemq /usr/local/apache-activemq/conf/log4j.properties
fi