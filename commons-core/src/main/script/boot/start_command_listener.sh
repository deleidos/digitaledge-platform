#!/bin/bash

# JMX Arguments needed by the ProcessGroupMonitor that runs in the Command Listener
export JMX_ARGS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=61515"

source /etc/rtwsrc

java -Xmx32M $JMX_ARGS -DRTWS_ROOT_LOG_LEVEL=$RTWS_ROOT_LOG_LEVEL -DRTWS_SAIC_LOG_LEVEL=$RTWS_SAIC_LOG_LEVEL -cp /usr/local/rtws/commons-core/lib/*: com.saic.rtws.commons.net.listener.CommandListener
