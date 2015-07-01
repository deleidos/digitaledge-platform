#!/bin/bash
set -x
# Configures the JobTracker

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

for dir in /mnt/rdafs/data/1/mapred/local /mnt/rdafs/data/2/mapred/local /mnt/rdafs/data/3/mapred/local
do
	if [ ! -d ${dir} ]; then
		mkdir -vp ${dir} ; chown -Rv mapred:hadoop ${dir}
	fi
done


echo "export RTWS_DOMAIN=$RTWS_DOMAIN" >> /etc/default/hadoop-0.20

# Wait for HDFS to become stable
su hdfs -l -s /bin/bash -c "hdfs dfsadmin -report"
IS_HDFS_READY=$?
MAX_CHECKS=10
CT=0
while (true)
do
	su hdfs -l -s /bin/bash -c "hdfs dfsadmin -report"
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
	
	    # Thought is to continue with boot which will start daemon monitors which should pick up the slack
		# in ensuring the region server daemons run.  This is to support the startup nature where the namenode/hbase master
		# is started by the master a period of time after this process group has started
        if [ $CT -gt $MAX_CHECKS ]; then
			echo "Max checks for hdfs health exceeded, continuing with boot process...."
			break 2
		fi
			
        (( CT += 1 ))
done

# Wait for the cluster to exit safemode
su hdfs -l -s /bin/bash -c "hdfs dfsadmin -safemode wait"

# Apply fix for jira issue with init.d scripts
perl -i -pe "s/PIDFILE=\"\/var\/run\/hadoop-0.20-mapreduce\/hadoop-hadoop-jobtracker.pid\"/PIDFILE=\"\/var\/run\/hadoop-0.20-mapreduce\/hadoop-mapred-jobtracker.pid\"/g" /etc/init.d/hadoop-0.20-mapreduce-jobtracker

# Wait until /tmp/ is writable
MAX_CHECKS=10
CT=0
while (true)
do	
	su mapred -l -s /bin/bash -c "hadoop fs -touchz /tmp/.jt_test"
	IS_HDFS_READY=$?
	if [ $IS_HDFS_READY -ne 0 ]; then
		echo "Waiting for the HDFS /tmp/ to become writable ...."
        sleep 1m
    else 
        break 2
    fi
    # Thought is to continue with boot which will start daemon monitors which should pick up the slack
	# in ensuring the region server daemons run.  This is to support the startup nature where the namenode/hbase master
	# is started by the master a period of time after this process group has started
    if [ $CT -gt $MAX_CHECKS ]; then
		echo "Max checks for hdfs health exceeded, continuing with boot process...."
		break 2
	fi
			
        (( CT += 1 ))
done


# Start jobtracker on first boot
chmod a+x /etc/init.d/hadoop-0.20-mapreduce-jobtracker
service hadoop-0.20-mapreduce-jobtracker start

# Start the secondary namenode
chmod a+x /etc/init.d/hadoop-hdfs-secondarynamenode
service hadoop-hdfs-secondarynamenode start