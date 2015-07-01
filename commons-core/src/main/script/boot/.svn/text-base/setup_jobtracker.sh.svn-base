#!/bin/bash
set -x
# Configures the JobTracker

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

for dir in /mnt/rdafs/data/1/mapred/local /mnt/rdafs/data/2/mapred/local /mnt/rdafs/data/3/mapred/local
do
	if [ ! -d ${dir} ]; then
		mkdir -vp ${dir} ; chown -Rv mapred:hadoop ${dir}
	fi
done


echo "export RTWS_DOMAIN=$RTWS_DOMAIN" >> /etc/default/hadoop-0.20

# Wait for HDFS to become stable
su hdfs -l -s /bin/bash -c "hadoop dfsadmin -report"
IS_HDFS_READY=$?
MAX_CHECKS=10
CT=0
while (true)
do
	su hdfs -l -s /bin/bash -c "hadoop dfsadmin -report"
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
chmod a+x /etc/init.d/hadoop-0.20-jobtracker
service hadoop-0.20-jobtracker start



# Temporary hook to configure the secondary namenode on the same process group instance
# TODO remove when new AMI is used which has this enabled by default
if [ ! -f /etc/init.d/hadoop-0.20-secondarynamenode ]; then
	echo "Creating /etc/init.d/hadoop-0.20-secondarynamenode..."
	cp -v /usr/local/rtws/commons-core/bin/boot/hadoop-0.20-secondarynamenode /etc/init.d/.
	fromdos /etc/init.d/hadoop-0.20-secondarynamenode
	chown root:root /etc/init.d/hadoop-0.20-secondarynamenode
	chmod a+x /etc/init.d/hadoop-0.20-secondarynamenode
	cd /etc/init.d/
	update-rc.d hadoop-0.20-secondarynamenode defaults
	cd -
fi

# Start the secondary namenode
chmod a+x /etc/init.d/hadoop-0.20-secondarynamenode
service hadoop-0.20-secondarynamenode start