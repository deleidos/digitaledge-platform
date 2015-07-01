#!/bin/bash
set -x

# Configures the Accumulo cluster

source /etc/rtwsrc

if [ "$(whoami)" != "root" ]; then
     echo "ERROR: You must be root to proceed..."
    exit 1
fi

sysctl -w vm.swappiness=0

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



# Set the instance name 
export INSTANCE_NAME="$RTWS_DOMAIN"


# Signal that this host is to be the master
export hst="$(curl -s -f http://169.254.169.254/latest/meta-data/local-hostname)"
echo -e "$hst" > /usr/lib/accumulo-1.4.1/conf/masters
echo -e "$hst" > /usr/lib/accumulo-1.4.1/conf/gc
echo -e "$hst" > /usr/lib/accumulo-1.4.1/conf/monitor
echo "" > /usr/lib/accumulo-1.4.1/conf/slaves
echo "" > /usr/lib/accumulo-1.4.1/conf/tracers

# Check for pre-initialized accumulo cluster
hadoop fs -test -d /accumulo/instance_id
if [ $? -ne 0 ]; then
	# Sets the instance name & root password
	su accumulo -l -s /bin/bash -c "cd /usr/lib/accumulo-1.4.1/ ; echo -e \"$INSTANCE_NAME\nchangeme\nchangeme\n\" | bin/accumulo init"
fi

# Start accumulo
su accumulo -l -s /bin/bash -c "cd /usr/lib/accumulo-1.4.1/ ; bin/start-all.sh"
