#!/bin/bash

pid=`ps auxwww | pgrep -f org.apache.directory.server.UberjarMain`

if [ -z "$pid" ]; then
  echo "ApacheDS is currently not running"
  exit 1
fi

# Send a nice shutdown message to all clients connected to the apacheds server

java -jar bin/apacheds-tools.jar graceful > logs/console.log 2>&1 &

# Wait 10 seconds for apacheds clients to finish there operations
# before forcefully killing the server

sleep 10;

ps auxwww | pkill -f org.apache.directory.server.UberjarMain
