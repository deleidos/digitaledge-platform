#!/bin/bash
set -x

# Configures the HBase Master

source /etc/rtwsrc

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

echo "export RTWS_DOMAIN=$RTWS_DOMAIN" >> /etc/default/hadoop-hbase

# Wait for HDFS to become stable
su hdfs -l -s /bin/bash -c "hadoop dfsadmin -report"
IS_HDFS_READY=$?
while (true)
do
	su hdfs -l -s /bin/bash -c "hadoop dfsadmin -report"
	IS_HDFS_READY=$?
	if [ $IS_HDFS_READY -ne 0 ]; then
		echo "Waiting for cluser to become healthy...."
        sleep 1m
    else
    	break
	fi	
done


# Give some time for namenode to startup to ensure the file system is not in safemode
IN_SAFEMODE=$(su hdfs -l -s /bin/bash -c "hadoop dfsadmin -report | grep \"Safe mode\"")
while (true)
do
        if [ ! -z "$IN_SAFEMODE" ]; then
           echo "Waiting for cluser to exit safe mode...."
           sleep 1m
           IN_SAFEMODE=$(su hdfs -l -s /bin/bash -c "hadoop dfsadmin -report | grep \"Safe mode\"")
        else
           	# TODO move this parsing to use the JMX Bean for cluster status
    		NUM_DATANODES=$(su hdfs -l -s /bin/bash -c 'hadoop dfsadmin -report' | grep Datanodes | awk -F: '{print $2}' | awk '{print $1}')
    		if [ $NUM_DATANODES -eq 0 ]; then
    		 	sleep 1m
    		else
    			break 2
    		fi
        fi
done

# Start hbase on first boot
chmod a+x /etc/init.d/hadoop-hbase-master
service hadoop-hbase-master start
