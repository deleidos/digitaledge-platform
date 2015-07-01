#!/bin/bash
#
# Expected AWS userdata parameters.
# (none)

if [[ "$(whoami)" != "root" ]]; then
     echo "ERROR: You must be root to proceed..."
     exit -1
fi

mkdir -p /usr/local/jetty
chown jetty:jetty /usr/local/jetty
chmod 755 /usr/local/jetty