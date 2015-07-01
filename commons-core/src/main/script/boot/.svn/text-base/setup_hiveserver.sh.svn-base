#!/bin/bash
set -x

# Configures the Hive server dependencies

source /etc/rtwsrc

# Wait for the cluster to exit safemode
su hdfs -l -s /bin/bash -c "hdfs dfsadmin -safemode wait"

su hdfs -l -s /bin/bash -c "hadoop fs -mkdir /user/hive"
su hdfs -l -s /bin/bash -c "hadoop fs -chown hive /user/hive"

# Wait until /user/hive is writable
while (true)
do	
	su hive -l -s /bin/bash -c "hadoop fs -touchz /user/hive/.hs_test"
	IS_HDFS_READY=$?
	if [ $IS_HDFS_READY -ne 0 ]; then
		echo "Waiting for the HDFS the /user/hive to become writable ...."
        sleep 1m
    else 
        break 2
    fi
done

# Give all users permission to read/write/delete from the hive warehouse folder corresponding to their table operations
su hive -l -s /bin/bash -c "hadoop fs -mkdir /user/hive/warehouse"
su hive -l -s /bin/bash -c "hadoop fs -chmod -R 777 /user/hive/warehouse"