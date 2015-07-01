#!/bin/bash
set -x
# Sets up a single node accumulo instance for datasink test purposes
# Assumes that this is running on a DigitalEdge Process Group instance

source /etc/rtwsrc

if [ "$(whoami)" != "root" ]; then
     echo "ERROR: You must be root to proceed..."
    exit 1
fi
          

# Add logger entry for datasink
if [ -f /usr/local/rtws/ingest/conf/log4j.properties ]; then
	echo 'log4j.logger.gov.navair=${RTWS_SAIC_LOG_LEVEL}, file' >> /usr/local/rtws/ingest/conf/log4j.properties
fi


# Only start a standalone accumulo if the configured datasink points to localhost for the zookeeper quorum
CONFIG_PREFIX="$(echo $RTWS_INGEST_CONFIG | sed "s/\.xml//g" | sed "s/services.//g" )"

RTWS_INGEST_CONFIG_DEFAULT="/usr/local/rtws/ingest/conf/pipeline.$CONFIG_PREFIX.default.xml"
PROCEED=1
if [ -f $RTWS_INGEST_CONFIG_DEFAULT ]; then
	
	ZOOKEEPER_REFERENCE="$(cat $RTWS_INGEST_CONFIG_DEFAULT | awk -F'<zookeeper-quorum>' '{print $2}' | awk -F'</zookeeper-quorum>' '{print $1}' | tr -sd '\n' '')"

	if [ ! -z $ZOOKEEPER_REFERENCE ]; then		
		echo "Datasink Zookeeper Quorum Property: ($ZOOKEEPER_REFERENCE)."
		# Grab user configured params needed by accumulo instnace
		INSTANCE_NAME="$(cat $RTWS_INGEST_CONFIG_DEFAULT | awk -F'<instance-name>' '{print $2}' | awk -F'</instance-name>' '{print $1}' | tr -sd '\n' '')"
		INSTANCE_USERNAME="$(cat $RTWS_INGEST_CONFIG_DEFAULT | awk -F'<username>' '{print $2}' | awk -F'</username>' '{print $1}' | tr -sd '\n' '')"
		INSTANCE_PASSWORD="$(cat $RTWS_INGEST_CONFIG_DEFAULT | awk -F'<password>' '{print $2}' | awk -F'</password>' '{print $1}' | tr -sd '\n' '')"
		
		if [ "$INSTANCE_NAME" == "local" ]; then
			echo "Setting up a standalone Accumulo on this datasink..."
			PROCEED=0
		else
			echo "Assuming that a Accumulo cluster exists."
		fi
	fi
fi


if [ $PROCEED -eq 0 ]; then
	echo 'export JAVA_HOME=/usr/lib/jvm/java-6-openjdk' > /etc/profile.d/jhome.sh
	chmod a+x /etc/profile.d/jhome.sh
	. /etc/profile.d/jhome.sh
	echo $JAVA_HOME
	
	echo "Setting up a standalone accumulo instance for testing purposes..."
	export CODENAME="$(cat /etc/lsb-release |grep DISTRIB_CODENAME | cut -d"=" -f2)"
	rm -f /etc/apt/sources.list.d/cloudera.list
	cat >> /etc/apt/sources.list.d/cloudera.list << "EOF"
deb http://archive.cloudera.com/debian CODENAME-cdh3u4 contrib
deb-src http://archive.cloudera.com/debian CODENAME-cdh3u4 contrib
EOF
	chown root:root /etc/apt/sources.list.d/cloudera.list
	perl -i -pe "s/CODENAME/$CODENAME/g" /etc/apt/sources.list.d/cloudera.list
	
	curl -s http://archive.cloudera.com/debian/archive.key | apt-key add -	
	apt-get update
	
	apt-get -y remove hadoop-hive hadoop-0.20-secondarynamenode hadoop-hbase-master hadoop-hbase-regionserver
	apt-get -y install hadoop-0.20-conf-pseudo hadoop-zookeeper-server
	echo '0' > /var/zookeeper/myid
	chown zookeeper:zookeeper /var/zookeeper/myid
	rm -f /etc/zookeeper/zoo.cfg
	cat >>/etc/zookeeper/zoo.cfg<<EOF
# The number of milliseconds of each tick
tickTime=2000
# The number of ticks that the initial
# synchronization phase can take
initLimit=10
# The number of ticks that can pass between
# sending a request and getting an acknowledgement
syncLimit=5
# the directory where the snapshot is stored.
dataDir=/var/zookeeper
# The max number of concurrent client connections
maxClientCnxns=60
# the minimum session timeout in milliseconds that the server will allow the client to negotiate.
minSessionTimeout=4000
#the maximum session timeout in milliseconds that the server will allow the client to negotiate.
maxSessionTimeout=80000
# the port at which the clients will connect
clientPort=2181
server.0=localhost:2888:3888
EOF
	chown root:root /etc/zookeeper/zoo.cfg
	
	perl -i -pe 's/# export JAVA_HOME=\/usr\/lib\/j2sdk1.6-sun/export JAVA_HOME=\/usr\/lib\/jvm\/java-6-openjdk/g' /etc/hadoop/conf/hadoop-env.sh
	mkdir -p /mnt/rdafs/hadoop ; chown root:hadoop /mnt/rdafs/hadoop ; \
	mv /var/lib/hadoop-0.20/cache /mnt/rdafs/hadoop/. ; ln -sf /mnt/rdafs/hadoop/cache /var/lib/hadoop-0.20/.
	
	cd /etc/init.d
	chmod a+x hadoop-zookeeper-server hadoop-0.20-namenode hadoop-0.20-jobtracker hadoop-0.20-datanode
	for sv in hadoop*
	do
		service $sv start
	done
	
	cd /mnt/rdafs/ ; wget http://www.motorlogy.com/apache/accumulo/1.4.1/accumulo-1.4.1-dist.tar.gz ; \
	tar -xzvf accumulo-1.4.1-dist.tar.gz ; ln -sf /mnt/rdafs/accumulo-1.4.1 /usr/lib/.
	
	cd /usr/lib/
	MEM="$(cat /proc/meminfo | grep MemTotal | awk '{print $2}')"
	if [ $MEM -lt 2097152 ]; then
		echo "Copying 512MB config"
		cp /usr/lib/accumulo-1.4.1/conf/examples/512MB/standalone/* /usr/lib/accumulo-1.4.1/conf/
	else
		echo "Copying 2GB config"
		cp /usr/lib/accumulo-1.4.1/conf/examples/2GB/standalone/* /usr/lib/accumulo-1.4.1/conf/
	fi
	cd -


	# Update the JAVA_HOME & HADOOP_HOME & ZOOKEEPER_HOME to their real locations   ($JAVA_HOME,/usr/lib/hadoop,/usr/lib/zookeeper)
	perl -i -pe 's/&& export JAVA_HOME=\/path\/to\/java/&& export JAVA_HOME=\/usr\/lib\/jvm\/java-6-openjdk/g' /usr/lib/accumulo-1.4.1/conf/accumulo-env.sh
	perl -i -pe 's/&& export HADOOP_HOME=\/path\/to\/hadoop/&& export HADOOP_HOME=\/usr\/lib\/hadoop/g' /usr/lib/accumulo-1.4.1/conf/accumulo-env.sh
	perl -i -pe 's/&& export ZOOKEEPER_HOME=\/path\/to\/zookeeper/&& export ZOOKEEPER_HOME=\/usr\/lib\/zookeeper/g' /usr/lib/accumulo-1.4.1/conf/accumulo-env.sh

	
	# Change default logging level to info 
	perl -i -pe 's/DEBUG/INFO/g' /usr/lib/accumulo-1.4.1/conf/generic_logger.xml
	perl -i -pe 's/DEBUG/INFO/g' /usr/lib/accumulo-1.4.1/conf/monitor_logger.xml
	
	# Sets the instance name & root password
	cd /usr/lib/accumulo-1.4.1/ ; echo -e "$INSTANCE_NAME\nchangeme\nchangeme\n" | bin/accumulo init
	perl -i -pe 's/\<value\>secret\<\/value\>/\<value\>changeme\<\/value\>/g' /usr/lib/accumulo-1.4.1/conf/accumulo-site.xml
	
	# Start accumulo
	cd /usr/lib/accumulo-1.4.1/ ; bin/start-all.sh
	
	# Update post-download based on conflicting deps
	echo -e '\n\nrm -fv /usr/local/rtws/ingest/lib/thrift-0.5.0.jar /usr/local/rtws/ingest/lib/thrift-fb303-0.5.0.jar' >> /usr/local/rtws/ingest/bin/post_download.sh
	chown rtws:rtws /usr/local/rtws/ingest/bin/post_download.sh
fi

# Create a user and set password  (Assumes that a running Accumulo is available either internal or standalone)
cd /usr/lib/accumulo-1.4.1/ ; echo -e "changeme\ncreateuser $INSTANCE_USERNAME\n$INSTANCE_PASSWORD\n$INSTANCE_PASSWORD\ngrant  System.CREATE_TABLE -s -u $INSTANCE_USERNAME\n" | bin/accumulo shell	