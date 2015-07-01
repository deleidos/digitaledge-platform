#!/bin/bash
set -x

source /etc/rtwsrc

if [[ "$(whoami)" != "root" ]]; then
     echo "ERROR: You must be root to proceed..."
     exit 1
fi

# Wait for the cluster to exit safemode
su hdfs -l -s /bin/bash -c "hdfs dfsadmin -safemode wait"

# Perform additional config of hue add-ons prior to starting hue
su hdfs -l -s /bin/bash -c "hadoop fs -mkdir /user/oozie"
su hdfs -l -s /bin/bash -c "hadoop fs -chown oozie /user/oozie"

# Wait until required directories are writable
REQUIRED_DIRS="/tmp /user/oozie"
for requiredDir in $REQUIRED_DIRS
do
   while (true)
   do	
	   su oozie -l -s /bin/bash -c "hadoop fs -touchz ${requiredDir}/.oozie_test"
	   IS_HDFS_READY=$?
	   if [ $IS_HDFS_READY -ne 0 ]; then
		   echo "Waiting for HDFS ${requiredDir} to become writable ...."
           sleep 1m
       else 
           break 2
       fi
    done
done

# Perform additional config of hue add-ons prior to starting hue
su oozie -l -s /bin/bash -c "hadoop fs -mkdir /user/oozie/share"
su oozie -l -s /bin/bash -c "hadoop fs -rm -r /user/oozie/share"

mkdir /tmp/ooziesharelib ; cd /tmp/ooziesharelib ; tar xzf /usr/lib/oozie/oozie-sharelib.tar.gz
su oozie -l -s /bin/bash -c "hadoop fs -put /tmp/ooziesharelib/share /user/oozie/"
rm -rf /tmp/ooziesharelib