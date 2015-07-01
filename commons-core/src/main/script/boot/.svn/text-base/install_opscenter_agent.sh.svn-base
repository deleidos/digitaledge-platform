#!/bin/bash
set -x

source /etc/rtwsrc

if [[ "$(whoami)" != "root" ]]; then
     echo "ERROR: You must be root to proceed..."
     exit 1
fi

echo "deb http://debian.datastax.com/community stable main" | sudo tee -a /etc/apt/sources.list.d/datastax.community.list
curl -L https://debian.datastax.com/debian/repo_key | sudo apt-key add -
apt-get update && apt-get install datastax-agent sysstat python-openssl
echo "stomp_interface: master.$RTWS_DOMAIN" | sudo tee -a /var/lib/datastax-agent/conf/address.yaml
service datastax-agent start