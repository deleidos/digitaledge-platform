#!/bin/bash
set -x

# Configures the NameNode and formats HDFS if requested

source /etc/rtwsrc

# Give the instance a N GB swap file
ephemeralSizeMB="$(du -ms /mnt 2> /dev/null | awk '{print $1}')"
swapFileSize="$(expr \( $ephemeralSizeMB \* 2 \) )"
dd if=/dev/zero of=/mnt/swapfile bs=$swapFileSize count=1048576
mkswap -f /mnt/swapfile
swapon /mnt/swapfile

# Set per doc rec: https://ccp.cloudera.com/display/CDH4DOC/Maintenance+Tasks+and+Notes#MaintenanceTasksandNotes-Settingthe%7B%7Bvm.swappiness%7D%7DLinuxKernelParameter
sysctl -w vm.swappiness=0

# Ensure nofile settings are applied
# TODO remove when added to base image
grep pam_limits.so /etc/pam.d/common-session
if [ $? -ne 0 ]; then
    echo 'session required  pam_limits.so' >> /etc/pam.d/common-session
fi

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
echo "export RTWS_DOMAIN=$RTWS_DOMAIN" >> /etc/default/hadoop-0.20

for dir in /mnt/rdafs/data/dfs/nn /mnt/rdafs/data/sn/dfs/nn
do
	if [ ! -d ${dir} ]; then
		mkdir -vp ${dir} ; chown -Rv hdfs:hdfs ${dir}
		# Correct permissions according to CDH3 Documentation
		chmod 700 /mnt/rdafs/data/dfs/nn /mnt/rdafs/data/sn/dfs/nn
		chmod go-rx /mnt/rdafs/data/dfs/nn /mnt/rdafs/data/sn/dfs/nn
	fi
done

if [ ! -f /mnt/rdafs/data/dfs/nn/current/VERSION ]; then	
	su hdfs  -s /bin/bash -c "echo -e \"Y\nY\n\"  | hdfs namenode -format"	
fi


# Start HDFS in order for these commands to function
chmod a+x /etc/init.d/hadoop-hdfs-namenode
service hadoop-hdfs-namenode start

# Wait until at least 1 data node is registered so filesystem setup can proceed without error
su hdfs -l -s /bin/bash -c 'hdfs dfsadmin -report'
IS_HDFS_READY=$?
while (true)
do
	su hdfs -l -s /bin/bash -c 'hdfs dfsadmin -report'
	IS_HDFS_READY=$?
	if [ $IS_HDFS_READY -ne 0 ]; then
		echo "Waiting for cluser to have at least 1 data node registered...."
        sleep 1m
    else
    	# TODO move this parsing to use the JMX Bean for cluster status
    	NUM_DATANODES=$(su hdfs -l -s /bin/bash -c 'hdfs dfsadmin -report' | grep Datanodes | awk -F: '{print $2}' | awk '{print $1}')
    	if [ $NUM_DATANODES -eq 0 ]; then
    		sleep 1m
    	else
    		break 2
    	fi
	fi	
done

# Wait for the cluster to exit safemode so that the file system setup below 
# is successfully propagated 
su hdfs -l -s /bin/bash -c "hdfs dfsadmin -safemode wait"

# Setup filesystem for mapreduce & hbase usage
su hdfs -l -s /bin/bash -c "hadoop fs -chmod 1777 /"
su hdfs -l -s /bin/bash -c "hadoop fs -mkdir /tmp"
su hdfs -l -s /bin/bash -c "hadoop fs -chmod -R 1777 /tmp"
su hdfs -l -s /bin/bash -c "hadoop fs -mkdir /tmp/mapred/system"
su hdfs -l -s /bin/bash -c "hadoop fs -chown mapred:hadoop /tmp/mapred/system"
su hdfs -l -s /bin/bash -c "hadoop fs -mkdir /var"
su hdfs -l -s /bin/bash -c "hadoop fs -mkdir /var/lib"
su hdfs -l -s /bin/bash -c "hadoop fs -mkdir /var/lib/hadoop-hdfs"
su hdfs -l -s /bin/bash -c "hadoop fs -mkdir -p /var/lib/hadoop-hdfs/cache/mapred/mapred/staging"
su hdfs -l -s /bin/bash -c "hadoop fs -chmod 1777 /var/lib/hadoop-hdfs/cache/mapred/mapred/staging"
su hdfs -l -s /bin/bash -c "hadoop fs -chown -R mapred /var/lib/hadoop-hdfs/cache/mapred"
su hdfs -l -s /bin/bash -c "hadoop fs -mkdir -p /tmp/hadoop-mapred/mapred/staging"
su hdfs -l -s /bin/bash -c "hadoop fs -chmod -R 1777 /tmp/hadoop-mapred/mapred"
su hdfs -l -s /bin/bash -c "hadoop fs -mkdir /user/rtws"
su hdfs -l -s /bin/bash -c "hadoop fs -chown rtws /user/rtws"
su hdfs -l -s /bin/bash -c "hadoop fs -mkdir /user/ubuntu"
su hdfs -l -s /bin/bash -c "hadoop fs -chown ubuntu /user/ubuntu"
su hdfs -l -s /bin/bash -c "hadoop fs -ls -R /"