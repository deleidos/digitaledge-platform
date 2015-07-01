#!/bin/bash
set -x

# Configures the Hadoop DataNode/TaskTracker

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

if [ -z $RTWS_MAX_ALLOCATION_REQUEST ]; then
	echo "Defaulting RTWS_MAX_ALLOCATION_REQUEST to 1"
	RTWS_MAX_ALLOCATION_REQUEST=1
fi

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

echo "export RTWS_DOMAIN=$RTWS_DOMAIN" >> /etc/default/hadoop-0.20

for dir in /mnt/rdafs/data/1/dfs/dn
do
	if [ ! -d ${dir} ]; then
mkdir -vp ${dir} ; chown -Rv hdfs:hdfs ${dir} ; chmod -Rv 700 ${dir}
	fi
done

for dir in /mnt/rdafs/data/1/mapred/local
do
	if [ ! -d ${dir} ]; then
		mkdir -vp ${dir} ; chown -Rv mapred:hadoop ${dir} ; chmod -Rv 755 ${dir}
	fi
done

# Apply fix for jira issue with init.d scripts
perl -i -pe "s/PIDFILE=\"\/var\/run\/hadoop-0.20-mapreduce\/hadoop-hadoop-tasktracker.pid\"/PIDFILE=\"\/var\/run\/hadoop-0.20-mapreduce\/hadoop-mapred-tasktracker.pid\"/g" /etc/init.d/hadoop-0.20-mapreduce-tasktracker


# Start the DataNode/TaskTracker daemons
chmod a+x /etc/init.d/hadoop-hdfs-datanode /etc/init.d/hadoop-0.20-mapreduce-tasktracker
service hadoop-hdfs-datanode start
service hadoop-0.20-mapreduce-tasktracker start