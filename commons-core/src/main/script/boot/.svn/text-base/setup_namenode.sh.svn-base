#!/bin/bash
set -x

# Configures the NameNode and formats HDFS if requested

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

echo "export RTWS_DOMAIN=$RTWS_DOMAIN" >> /etc/default/hadoop-0.20

for dir in /mnt/rdafs/data/dfs/nn /mnt/rdafs/data/sn/dfs/nn
do
	if [ ! -d ${dir} ]; then
		mkdir -vp ${dir} ; chown -Rv hdfs:hadoop ${dir}
		# Correct permissions according to CDH3 Documentation
		chmod 700 /mnt/rdafs/data/dfs/nn /mnt/rdafs/data/sn/dfs/nn
		chmod go-rx /mnt/rdafs/data/dfs/nn /mnt/rdafs/data/sn/dfs/nn
	fi
done

if [ ! -f /mnt/rdafs/data/dfs/nn/current/VERSION ]; then	
	su hdfs -l -s /bin/bash -c "echo -e \"Y\nY\n\"  | /usr/lib/hadoop/bin/hadoop namenode -format"	
fi


# Start HDFS in order for these commands to function
chmod a+x /etc/init.d/hadoop-0.20-namenode
service hadoop-0.20-namenode start

# Wait until at least 1 data node is registered so filesystem setup can proceed without error
su hdfs -l -s /bin/bash -c 'hadoop dfsadmin -report'
IS_HDFS_READY=$?
while (true)
do
	su hdfs -l -s /bin/bash -c 'hadoop dfsadmin -report'
	IS_HDFS_READY=$?
	if [ $IS_HDFS_READY -ne 0 ]; then
		echo "Waiting for cluser to have at least 1 data node registered...."
        sleep 1m
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

# Setup filesystem for mapreduce & hbase usage
su hdfs -l -s /bin/bash -c "hadoop fs -mkdir /tmp"
su hdfs -l -s /bin/bash -c "hadoop fs -chmod -R 1777 /tmp"
su hdfs -l -s /bin/bash -c "hadoop fs -chmod 1777 /"
su hdfs -l -s /bin/bash -c "hadoop fs -mkdir /mapred/system"
su hdfs -l -s /bin/bash -c "hadoop fs -chown mapred:hadoop /mapred/system"