#!/bin/bash

# Load the RTWS Environment variables

. /etc/rtwsrc

export JETTY_HOME=/usr/local/jetty
export JETTY_USER=root
export JETTY_PORT=80
export TMPDIR=/tmp

sudo cp ../start_template.ini ../start.ini
sudo chown jetty:jetty ../start.ini

sudo sed -i '/JVM_ARGS_MARKER/i\-Xdebug' ../start.ini
sudo sed -i '/JVM_ARGS_MARKER/i\-Xnoagent' ../start.ini
sudo sed -i '/JVM_ARGS_MARKER/i\-Xrunjdwp:transport=dt_socket,address=8080,server=y,suspend=n' ../start.ini
sudo sed -i '/JVM_ARGS_MARKER/i\-DRTWS_TENANT_ID='"$RTWS_TENANT_ID"'' ../start.ini
sudo sed -i '/JVM_ARGS_MARKER/i\-DRTWS_FQDN='"$RTWS_FQDN"'' ../start.ini
sudo sed -i '/JVM_ARGS_MARKER/i\-DRTWS_ROOT_LOG_LEVEL='"$RTWS_ROOT_LOG_LEVEL"'' ../start.ini
sudo sed -i '/JVM_ARGS_MARKER/i\-DRTWS_APP_LOG_LEVEL='"$RTWS_APP_LOG_LEVEL"'' ../start.ini
sudo sed -i '/JVM_ARGS_MARKER/i\-DRTWS_SW_VERSION='"$RTWS_SW_VERSION"'' ../start.ini
sudo sed -i '/JVM_ARGS_MARKER/d' ../start.ini

ulimit -n 20480

for webapp in $(/bin/ls -d /usr/local/jetty/webapps/*)
do
	appName=$(basename $webapp)
	if [ ! -d /mnt/$appName ]; then
		mkdir -p /mnt/$appName ; chown jetty:jetty /mnt/$appName
	fi
done

./jetty.sh start 
