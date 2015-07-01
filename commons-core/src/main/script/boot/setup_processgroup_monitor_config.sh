#!/bin/bash

# Launcher for Digital Edge System Monitoring components

if [ "$(whoami)" != "root" ]; then
  echo "ERROR: You must be root to proceed..."
  exit 1
fi

if [ -z $1 ]; then
  echo "USAGE: $0 <monitor class name>,<monitor class name>,<monitor class name>,..."
  exit 1
fi

# Create the configuration file to store the monitors in
CONFIG_FILE="/usr/local/rtws/conf/monitored_processes.conf"
if [ ! -f $CONFIG_FILE ]; then
  touch $CONFIG_FILE
fi

# Split the CSV argument into different parts 
IFS=',' read -ra MONITORS <<< "$1"
for MONITOR in "${MONITORS[@]}"; do
  echo $MONITOR >> $CONFIG_FILE
done

# Modify the access of the config file
chmod 444 $CONFIG_FILE