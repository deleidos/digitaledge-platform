#!/bin/bash

pid=`ps auxwww | pgrep -f org.apache.directory.server.UberjarMain`

if [ -z "$pid" ]; then
  echo "ApacheDS is currently not running"
  exit 1
fi

echo "Stopping ApacheDS with PID $pid"
while [ ! -z "$pid" ]; do
  ps auxwww | pkill -f org.apache.directory.server.UberjarMain
  pid=`ps auxwww | pgrep -f org.apache.directory.server.UberjarMain`
  if [ ! -z "$pid" ]; then
  	sleep 2
    echo -n .
  fi
done
