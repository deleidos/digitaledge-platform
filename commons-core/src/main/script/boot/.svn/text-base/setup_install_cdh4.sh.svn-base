#!/bin/bash
set -x

# Upgrades CDH3 to CDH4 (Scale2Insight depends on Hbase 0.92)
# Uses MapReduce V1 vs Yarn (MapReduce V2)

source /etc/rtwsrc


# Uninstall CDH3 components
apt-get -y purge hadoop-0.20 hadoop-0.20-namenode hadoop-0.20-secondarynamenode hadoop-hbase-master \
		hadoop-0.20-jobtracker hadoop-zookeeper-server hadoop-0.20-datanode \
		hadoop-0.20-tasktracker hadoop-hbase-regionserver hadoop-hive
apt-get -y autoremove
apt-get remove cdh3-repository
rm -fv /etc/init.d/hadoop-*

# Install CDH4  (TODO - removed when added to AMI)
# Unset JAVA_HOME to keep deb installers from successfully starting services
export JAVA_HOME=

cd /tmp ; wget http://archive.cloudera.com/cdh4/one-click-install/squeeze/amd64/cdh4-repository_1.0_all.deb ; dpkg -i /tmp/cdh4-repository_1.0_all.deb

curl -s http://archive.cloudera.com/cdh4/debian/squeeze/amd64/cdh/archive.key | sudo apt-key add -
apt-get -y update
apt-get -y install hadoop hadoop-hdfs  hadoop-hdfs-namenode hadoop-hdfs-secondarynamenode \
		hadoop-0.20-mapreduce-jobtracker hadoop-0.20-mapreduce-tasktracker hadoop-hdfs-datanode \
		hadoop-hdfs-journalnode hadoop-hdfs-zkfc hadoop-mapreduce hadoop-mapreduce-historyserver \
		hadoop-yarn hadoop-yarn-nodemanager hadoop-yarn-proxyserver  hadoop-yarn-resourcemanager \
		zookeeper zookeeper-server hbase hbase-master hbase-regionserver  \
		hive hive-metastore hive-server


# Disable auto start of all hadoop components  (setup_<service> will renable the scripts it uses)
cd /etc/init.d
for sv in hadoop*
do
	service $sv stop
	chmod a-x $sv
done

# Current hadoop user env expects this location, so create it
mkdir /.tmpdirs