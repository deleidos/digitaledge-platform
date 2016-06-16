#!/bin/bash

pid=`ps auxwww | pgrep -f org.apache.directory.server.UberjarMain`

if [ ! -z "$pid" ]; then
  echo "ApacheDS is currently running with pid $pid"
  exit 1
fi

# Checking the number of parameters
if [ $# -eq 0 ]
then
    # Using '/mnt/rdafs/apacheds' as default working directory
    WORKING_DIR="/mnt/rdafs/apacheds"
elif [ $# -eq 1 ]
then
    # Getting the working directory from the arguments
    WORKING_DIR=$1
else
    # Printing usage information
    echo "Usage: $0 <working directory>"
    echo "If <working directory> is ommited, '/mnt/rdafs/apacheds' will be used."
    exit 1
fi

export _JAVA_OPTIONS="-Xms64m -Xmx256m"

bin/apacheds.sh $WORKING_DIR > logs/console.log 2>&1 &
