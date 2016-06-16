#!/bin/bash

# Simple script to configure then start a container appliance image
# it's basic and should be evolved/replaced when a better approach is available
# Assumes env variables are passed in via docker's environment variable feature
# https://docs.docker.com/reference/run/#env-environment-variables

SHUTDOWN=false

_term() { 
  printf "%s\n" "Caught SIGTERM signal!" 
  kill -TERM $child 2>/dev/null
  SHUTDOWN=true
}

trap _term 15


_busywork() {
	while [ "$SHUTDOWN" != "true" ]
	do
		sleep 1m
	done
	_gracefulShutdown
}

_gracefulShutdown() {
	export RTWS_SHUTDOWN=true
	cd /usr/local/rtws/commons-core/bin/boot/appliance
	ansible-playbook shutdown.yml -c local --tags "shutdown"
}


# Hook for image building without re-defining the entrypoint
if [ ! -z "$RTWS_BYPASS" ]; then
	_busywork
fi


# Expload the necessary files
if [ ! -f /usr/local/rtws/ ]; then
	mkdir -p /usr/local/rtws/
fi

# Expload core since that is currently where the initialization magic is located
tar -C /usr/local/rtws/ -xzvf /mnt/appfs/release/$RTWS_RELEASE/deleidos-rtws-commons-core.tar.gz

# Not sure why they are not already readable by all
chmod a+r /etc/hosts /etc/resolv.conf

# Configure the process group which should also start the required apps via playbook
cd /usr/local/rtws/commons-core/bin/boot/appliance
ansible-playbook site.yml -c local

if [ $? -ne 0 ]; then
	echo "ERROR: failed to configure the container properly."
	exit 1
fi

# simple hook to keep the successfully booted container running 
_busywork