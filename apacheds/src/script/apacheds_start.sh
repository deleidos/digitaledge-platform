#!/bin/bash

pid=`ps auxwww | pgrep -f org.apache.directory.server.UberjarMain`

if [ ! -z $pid ]; then
  echo "ApacheDS is currently running with pid $pid"
  exit 1
fi

export _JAVA_OPTIONS="-Xms64m -Xmx256m"

./apacheds.sh > logs/console.log 2>&1 &
