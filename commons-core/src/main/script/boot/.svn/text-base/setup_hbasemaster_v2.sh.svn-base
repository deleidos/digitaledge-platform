#!/bin/bash
set -x

# Configures the HBase Master

source /etc/rtwsrc

# Set per doc rec: https://ccp.cloudera.com/display/CDH4DOC/Maintenance+Tasks+and+Notes#MaintenanceTasksandNotes-Settingthe%7B%7Bvm.swappiness%7D%7DLinuxKernelParameter
sysctl -w vm.swappiness=0

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

echo "export RTWS_DOMAIN=$RTWS_DOMAIN" >> /etc/default/hadoop-hbase

# Wait for HDFS to become stable
su hdfs -l -s /bin/bash -c "hdfs dfsadmin -report"
IS_HDFS_READY=$?
while (true)
do
	su hdfs -l -s /bin/bash -c "hdfs dfsadmin -report"
	IS_HDFS_READY=$?
	if [ $IS_HDFS_READY -ne 0 ]; then
		echo "Waiting for cluser to become healthy...."
        sleep 1m
    else
    	break
	fi	
done


# Give some time for namenode to startup to ensure the file system is not in safemode
su hdfs -l -s /bin/bash -c "hdfs dfsadmin -safemode wait"

# Start hbase on first boot
chmod a+x /etc/init.d/hbase-master /etc/init.d/hbase-thrift
service hbase-master start
sleep 1m
service hbase-thrift start