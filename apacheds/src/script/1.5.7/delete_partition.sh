#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Usage: $0 <partition-name>"
    exit 1
fi

PARTITION="$1"

SERVER_FILE="/usr/local/apacheds/conf/server.xml"

# Remove the partition from the server configuration
sed -i '/'"$PARTITION"'/d' "$SERVER_FILE"

fromdos "$SERVER_FILE"

# Restart the server
echo "Stopping ApacheDS server"
sudo "/usr/local/apacheds/apacheds_stop.sh"
pid=`ps auxwww | pgrep -f org.apache.directory.server.UberjarMain`
while [ ! -z $pid ]; do
  # Wait until the server stops to restart it
  echo "."
  sleep 2
  pid=`ps auxwww | pgrep -f org.apache.directory.server.UberjarMain`
done

sudo "/usr/local/apacheds/apacheds_start.sh"
