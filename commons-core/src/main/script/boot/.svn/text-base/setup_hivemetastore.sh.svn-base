#!/bin/bash
set -x

# Configures the Hive metastore (if applicable)

source /etc/rtwsrc

INIT_TAG=/mnt/rdafs/.metastore_initialized.txt

if [[ ! -f $INIT_TAG ]]; then
	java -cp /usr/local/rtws/commons-core/lib/h2*.jar org.h2.tools.RunScript -url jdbc:h2:tcp://127.0.0.1:8161/commondb -user sa -script /usr/local/rtws/commons-core/bin/boot/h2_create_hive_metatastore.sql
	if [ $? -eq 0 ]; then
		date > $INIT_TAG
	fi	
fi

# Wait for the cluster to exit safemode
su hdfs -l -s /bin/bash -c "hdfs dfsadmin -safemode wait"


su hdfs -l -s /bin/bash -c "hadoop fs -mkdir /user/hive"
su hdfs -l -s /bin/bash -c "hadoop fs -chown hive /user/hive"

# Wait until /user/hive is writable
while (true)
do	
	su hive -l -s /bin/bash -c "hadoop fs -touchz /user/hive/.hm_test"
	IS_HDFS_READY=$?
	if [ $IS_HDFS_READY -ne 0 ]; then
		echo "Waiting for the HDFS the /user/hive to become writable ...."
        sleep 1m
    else 
        break 2
    fi
done

# Give all users permission to read/write/delete from the hive warehouse folder corresponding to their table operations
su hive -l -s /bin/bash -c "hadoop fs -chmod -R 777 /user/hive/warehouse"

# Wait until /user/hive is writable
while (true)
do	
	su hive -l -s /bin/bash -c "hadoop fs -mkdir -p /tmp/hive/aux"
	IS_HDFS_READY=$?
	if [ $IS_HDFS_READY -ne 0 ]; then
		echo "Waiting for successful creation of /tmp/hive/aux ...."
        sleep 1m
    else 
        break 2
    fi
done
su hive -l -s /bin/bash -c "hadoop fs -chmod -R 777 /tmp/hive/aux"


# Copy over the serde(s)
for sde in $(find /usr/local/hive-config-v2* -type f -name '*.jar' | grep serde)
do
   su hive -l -s /bin/bash -c "hadoop fs -put $sde /tmp/hive/aux"
done

# Copy up jars to support Hive over Hbase Queries
for jr in zookeeper.jar hbase.jar hive-hbase-handler-0.10.0-cdh4.3.0.jar guava-11.0.2.jar
do
	su hive -l -s /bin/bash -c "hadoop fs -put /usr/lib/hive/lib/$jr /tmp/hive/aux"
done