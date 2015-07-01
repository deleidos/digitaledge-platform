#!/bin/bash
set -x

# Configures the Hadoop DataNode/TaskTracker

source /etc/rtwsrc

# TODO replace this really bad way of getting the release we're running.....
RELEASE="$(grep commons-core $RTWS_MANIFEST | grep INSTALL | awk -F"/" '{print $5}')"

# If lsb-release file is present, assume Ubuntu
if [[ -f /etc/lsb-release ]]; then
	
			# If profile.d script is there to set JAVA_HOME, use it or create it
	if [[ -f /etc/profile.d/jdk.sh ]]; then
		. /etc/profile.d/jdk.sh
	else
		rm -f /etc/profile.d/jdk.sh
    	cat >> /etc/profile.d/jdk.sh << "EOF"
export JAVA_HOME=/usr/lib/jvm/java-6-openjdk
export PATH=$JAVA_HOME/bin:$PATH
EOF

		chmod a+x /etc/profile.d/jdk.sh ; chown root:root /etc/profile.d/jdk.sh
		. /etc/profile.d/jdk.sh
	fi
fi


# If redhat-release file is present, assume Redhat/Centos
if [[ -f /etc/redhat-release ]]; then
	echo "nothing to do anymore for Centos :)"
fi


echo "export RTWS_DOMAIN=$RTWS_DOMAIN" >> /etc/default/hadoop-0.20

for dir in /mnt/rdafs/data/1/dfs/dn
do
	if [ ! -d ${dir} ]; then
		mkdir -vp ${dir} ; chown -Rv hdfs:hadoop ${dir} ; chmod -Rv 700 ${dir}
	fi
done

for dir in /mnt/rdafs/data/1/mapred/local
do
	if [ ! -d ${dir} ]; then
		mkdir -vp ${dir} ; chown -Rv mapred:hadoop ${dir} ; chmod -Rv 755 ${dir}
	fi
done

# Start the DataNode/TaskTracker daemons
chmod a+x /etc/init.d/hadoop-0.20-datanode /etc/init.d/hadoop-0.20-tasktracker
service hadoop-0.20-datanode start
service hadoop-0.20-tasktracker start