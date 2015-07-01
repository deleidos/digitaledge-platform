#!/bin/bash
set -x

source /etc/rtwsrc

if [[ "$(whoami)" != "root" ]]; then
     echo "ERROR: You must be root to proceed..."
     exit 1
fi

echo "deb http://debian.datastax.com/community stable main" | sudo tee -a /etc/apt/sources.list.d/datastax.community.list
curl -L http://debian.datastax.com/debian/repo_key | apt-key add -
apt-get update && apt-get install opscenter python-openssl libffi-dev
easy_install pyOpenSSL
/usr/share/opscenter/bin/setup.py
echo '[agents]' >> /etc/opscenter/opscenterd.conf
echo 'use_ssl = true' >> /etc/opscenter/opscenterd.conf
service opscenterd start