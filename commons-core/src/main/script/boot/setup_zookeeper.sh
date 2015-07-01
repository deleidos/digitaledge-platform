#!/bin/bash

# Configures the Zookeeper Quorum

source /etc/rtwsrc

if [ -z $RTWS_MAX_ALLOCATION_REQUEST ]; then
	echo "Defaulting RTWS_MAX_ALLOCATION_REQUEST to 1"
	RTWS_MAX_ALLOCATION_REQUEST=1
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


# If redhat-release file is present, assume Redhat/Centos
if [[ -f /etc/redhat-release ]]; then
	echo "nothing to do anymore for Centos :)"	
fi

# Set RTWS_ZOOKEEPER_ID based on parsing master supplied RTWS_FQDN
RTWS_ZOOKEEPER_ID="$(cat /etc/rtwsrc | grep RTWS_FQDN | awk -F= '{print $2}' | awk -F. '{print $1}' | tr -d '[a-z]')"

# Initialize server id file
rm -rf /var/zookeeper/*
echo $RTWS_ZOOKEEPER_ID > /var/zookeeper/myid
chown zookeeper:zookeeper /var/zookeeper/myid

# Create initial zoo.cfg
echo "Generated on $(date)" > /etc/zookeeper/zoo.cfg
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
maxClientCnxns=150
# the minimum session timeout in milliseconds that the server will allow the client to negotiate.
minSessionTimeout=4000
#the maximum session timeout in milliseconds that the server will allow the client to negotiate.
maxSessionTimeout=80000
# the port at which the clients will connect
clientPort=2181
EOF

# Configure N number of servers in the quorum
x=1
while (( x <= $RTWS_MAX_ALLOCATION_REQUEST ))
do
	echo "server.${x}=zookeeper${x}.$RTWS_DOMAIN:2888:3888" >> /etc/zookeeper/zoo.cfg
	(( x += 1 ))
done

chown root:root /etc/zookeeper/zoo.cfg

# Generate zookeeper-server script to add JVM flags
# TODO Go back and figure out how to inject them without modifying this file, or watch for a JIRA which allows it to be sourced from a /etc/sysconfig file
rm -f /usr/bin/zookeeper-server
cat >>/usr/bin/zookeeper-server<<"EOF"
#!/bin/sh
export ZOOPIDFILE=${ZOOPIDFILE:-/var/run/zookeeper/zookeeper-server.pid}
export ZOOKEEPER_HOME=${ZOOKEEPER_CONF:-/usr/lib/zookeeper}
export ZOOKEEPER_CONF=${ZOOKEEPER_CONF:-/etc/zookeeper}
export ZOOCFGDIR=$ZOOKEEPER_CONF
export CLASSPATH=$CLASSPATH:$ZOOKEEPER_CONF:$ZOOKEEPER_HOME/*:$ZOOKEEPER_HOME/lib/*
export ZOO_LOG_DIR=/var/log/zookeeper
export ZOO_LOG4J_PROP=INFO,ROLLINGFILE
export JVMFLAGS="-Dzookeeper.log.threshold=INFO -Xmx1024m -ea -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode"
env CLASSPATH=$CLASSPATH /usr/lib/zookeeper/bin/zkServer.sh "$@"
EOF

chmod 755 /usr/bin/zookeeper-server ; chown root:root /usr/bin/zookeeper-server


# Start Zookeeper
chmod a+x /etc/init.d/hadoop-zookeeper-server 
service hadoop-zookeeper-server start