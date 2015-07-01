#!/bin/bash
set -x

source /etc/rtwsrc

# TODO Remove when Hue is baked into the AMI

USE_VERSION_2x=$1

if [[ "$(whoami)" != "root" ]]; then
     echo "ERROR: You must be root to proceed..."
     exit 1
fi

apt-get -y update

if [[ -z "$USE_VERSION_2x" ]]; then
   	echo "Using the Hue 1.2x release from the base cdh3u4 install"
   	
   	# Install hue, server & plugins accordingly
	if [[ "$RTWS_PROCESS_GROUP" == "hue.server" ]]; then
			apt-get -y install hue hue-server hue-plugins hadoop-pig
	else
		apt-get -y install hue-plugins
	fi
	
else 
	echo "Uinsg the Hue 2.0.1 release from tarball install"


	# Install hue version 2.0.1 which is the last version compatible with cdh3u4.  
	# The version in cdh3 is old as dog poo.....
	cd /usr/lib/hive/lib
	wget -q -o libthrift-0.9.0.jar http://search.maven.org/remotecontent?filepath=org/apache/thrift/libthrift/0.9.0/libthrift-0.9.0.jar
	rm -f libthrift.jar
	ln -sf libthrift-0.9.0.jar libthrift.jar


	cd /usr/local

	wget -q http://mirror.metrocast.net/apache/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.zip
	unzip apache-maven-3.0.5-bin.zip
	export M2_HOME=/usr/local/apache-maven-3.0.5
	export PATH=$PATH:$M2_HOME/bin

	adduser --system --disabled-login --group --home /usr/share/hue --gecos "Hue daemon" hue
	
	wget -q https://github.com/downloads/cloudera/hue/hue-2.0.1.tgz ; tar -xzvf  hue-2.0.1.tgz

	cd hue-2.0.1
	apt-get -y install python-dev python-setuptools python-simplejson libsqlite3-dev libsqlite3-dev libxml2-dev libxslt-dev libsasl2-dev libsasl2-modules-gssapi-mit libmysqlclient-dev ant python-utidylib
	
	PREFIX=/usr/share make install
	cd /usr/share/hue
	chmod 4750 apps/shell/src/shell/build/setuid
	
	cp desktop/libs/hadoop/java-lib/hue-plugins-*.jar /usr/lib/hadoop/lib
	
	# cleanup
	rm -rf /usr/local/hue*
	
	cp -f /etc/hue/conf/* ./desktop/conf/.
	
	# Start up everything only if on hue.server process group instance
	if [[ "$RTWS_PROCESS_GROUP" == "hue.server" ]]; then
			build/env/bin/supervisor
	fi
fi