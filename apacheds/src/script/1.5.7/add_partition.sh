#!/bin/bash

if [ $# -ne 2 ]; then
    echo "Usage: $0 <new-partition-name> <after-partition-name>"
    exit 1
fi

NEW_PARTITION="$1"
AFTER_PARTITION="$2"

SERVER_FILE="/usr/local/apacheds/conf/server.xml"

# Make sure to create a server partition for the new context
sed -i '/<jdbmPartition id="'"$AFTER_PARTITION"'" /a\\n      <!-- '"$NEW_PARTITION"' system partition -->\n      <jdbmPartition id=\"'"$NEW_PARTITION"'\" cacheSize=\"100\" suffix=\"dc='"$NEW_PARTITION"',dc=com\" optimizerEnabled=\"true\" syncOnWrite=\"true\" \/>' "$SERVER_FILE"

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
