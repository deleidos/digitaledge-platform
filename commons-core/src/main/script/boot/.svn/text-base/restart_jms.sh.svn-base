#!/bin/bash

# Helper script to restart JMS functions

source /etc/rtwsrc

if [[ "$(whoami)" != "root" ]]; then
     echo "ERROR: You must be root to proceed..."
     exit 1
fi

su rtws -l -s /bin/bash -c "cd /usr/local/rtws/jms-router/bin/ ; /usr/local/rtws/jms-router/bin/init_jms_router.sh stop"
sleep 1m
su activemq -l -s /bin/bash -c "cd /usr/local/rtws/commons-core/bin/boot/ ; /usr/local/rtws/commons-core/bin/boot/activemq_stop.sh"


su root -l -s /bin/bash -c "cd /usr/local/rtws/commons-core/bin/boot/ ; /usr/local/rtws/commons-core/bin/boot/start_activemq.sh"
sleep 5m
su rtws -l -s /bin/bash -c "cd /usr/local/rtws/jms-router/bin/ ; /usr/local/rtws/jms-router/bin/init_jms_router.sh start"