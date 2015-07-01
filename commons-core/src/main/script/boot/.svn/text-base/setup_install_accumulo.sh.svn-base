#!/bin/bash
set -x

# Installs Accumulo
# TODO Remove when baked into the ami

source /etc/rtwsrc

if [ "$(whoami)" != "root" ]; then
     echo "ERROR: You must be root to proceed..."
    exit 1
fi

echo 'export JAVA_HOME=/usr/lib/jvm/java-6-openjdk' > /etc/profile.d/jhome.sh
chmod a+x /etc/profile.d/jhome.sh
. /etc/profile.d/jhome.sh
echo $JAVA_HOME

# TODO Remove when baked into the AMI
cd /usr/lib/ ; wget http://www.motorlogy.com/apache/accumulo/1.4.1/accumulo-1.4.1-dist.tar.gz ; \
tar -xzvf accumulo-1.4.1-dist.tar.gz ; chown -R root:root /usr/lib/accumulo-1.4.1