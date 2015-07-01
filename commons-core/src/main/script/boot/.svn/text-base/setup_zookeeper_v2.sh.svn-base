#!/bin/bash

# Configures the Zookeeper Quorum

source /etc/rtwsrc

# Set per doc rec: https://ccp.cloudera.com/display/CDH4DOC/Maintenance+Tasks+and+Notes#MaintenanceTasksandNotes-Settingthe%7B%7Bvm.swappiness%7D%7DLinuxKernelParameter
sysctl -w vm.swappiness=0

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

# Set RTWS_ZOOKEEPER_ID based on parsing master supplied RTWS_FQDN
RTWS_ZOOKEEPER_ID="$(cat /etc/rtwsrc | grep RTWS_FQDN | awk -F= '{print $2}' | awk -F. '{print $1}' | tr -d '[:alpha:]')"

# Create initial zoo.cfg
echo "# Generated on $(date)" > /etc/zookeeper/conf/zoo.cfg
cat >>/etc/zookeeper/conf/zoo.cfg<<EOF
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
	echo "server.${x}=zookeeper${x}.$RTWS_DOMAIN:2888:3888" >> /etc/zookeeper/conf/zoo.cfg
	(( x += 1 ))
done

chown root:root /etc/zookeeper/conf/zoo.cfg

# Generate zookeeper-server script to add JVM flags
# TODO Go back and figure out how to inject them without modifying this file, or watch for a JIRA which allows it to be sourced from a /etc/sysconfig file
cp -fv /usr/bin/zookeeper-server /usr/bin/zookeeper-server.save
rm -f /usr/bin/zookeeper-server
cat >>/usr/bin/zookeeper-server<<"EOF"
#!/bin/sh

# Autodetect JAVA_HOME if not defined
. /usr/lib/bigtop-utils/bigtop-detect-javahome

export ZOOPIDFILE=${ZOOPIDFILE:-/var/run/zookeeper/zookeeper-server.pid}
export ZOOKEEPER_HOME=${ZOOKEEPER_CONF:-/usr/lib/zookeeper}
export ZOOKEEPER_CONF=${ZOOKEEPER_CONF:-/etc/zookeeper/conf}
export ZOOCFGDIR=${ZOOCFGDIR:-$ZOOKEEPER_CONF}
export CLASSPATH=$CLASSPATH:$ZOOKEEPER_CONF:$ZOOKEEPER_HOME/*:$ZOOKEEPER_HOME/lib/*
export ZOO_LOG_DIR=${ZOO_LOG_DIR:-/var/log/zookeeper}
export ZOO_LOG4J_PROP=${ZOO_LOG4J_PROP:-INFO,ROLLINGFILE}
export JVMFLAGS="-Xmx1024m -ea -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -Dzookeeper.log.threshold=INFO"
export ZOO_DATADIR_AUTOCREATE_DISABLE=${ZOO_DATADIR_AUTOCREATE_DISABLE:-true}
env CLASSPATH=$CLASSPATH /usr/lib/zookeeper/bin/zkServer.sh "$@"

EOF

chmod 755 /usr/bin/zookeeper-server ; chown root:root /usr/bin/zookeeper-server


# Initialize server id file & Start Zookeeper
chmod a+x /etc/init.d/zookeeper-server
rm -rf /var/zookeeper/*
mkdir -p /var/zookeeper/; chown -R zookeeper:zookeeper /var/zookeeper
service zookeeper-server init
echo $RTWS_ZOOKEEPER_ID > /var/zookeeper/myid
chown zookeeper:zookeeper /var/zookeeper/myid
service zookeeper-server start