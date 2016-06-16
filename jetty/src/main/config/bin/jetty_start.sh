#!/bin/bash

# Load the RTWS Environment variables

. /etc/rtwsrc

export JETTY_HOME=/usr/local/jetty
export JETTY_USER=root
export JETTY_PORT=80
export TMPDIR=/tmp

sudo cp ../start_template.ini ../start.ini
sudo chown jetty:jetty ../start.ini

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

# Determine the % of memory to allocate to jetty
nodebasename=`echo $RTWS_FQDN | awk -F. '{ print $1 }'`
if [ "$nodebasename" == "gateway" -o "$nodebasename" == "auth" -o "$nodebasename" == "tmsp" ]
then
    # On the gateway and tms auth node most of the memory goes to the database
    jettypercent="25"
else
    jettypercent="58"
fi

totalMemory=`free -m | grep "^Mem" | awk '{print $2}'`
maxsize=`expr \( \( $totalMemory \* $jettypercent \) / 100 \)`
inisize=`expr $maxsize / 2`
maxsize=$maxsize'm'
inisize=$inisize'm'

cp $JETTY_HOME/start.ini /tmp
sed "s/^\-Xmx.*$/-Xmx$maxsize/" < /tmp/start.ini | sed "s/^\-Xms.*$/-Xms$inisize/" > $JETTY_HOME/start.ini
rm /tmp/start.ini

./jetty.sh start
