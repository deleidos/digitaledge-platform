#!/bin/bash

# Installs the Ganglia web frontend
# TODO Remove when baked into the ami

source /etc/rtwsrc

if [ "$(whoami)" != "root" ]; then
     echo "ERROR: You must be root to proceed..."
    exit 1
fi

apt-get -y install ganglia-webfrontend