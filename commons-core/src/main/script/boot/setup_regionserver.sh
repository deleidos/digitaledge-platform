#!/bin/bash
set -x
# Configures the HBase Region Server

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

echo "export RTWS_DOMAIN=$RTWS_DOMAIN" >> /etc/default/hadoop-hbase

# Give some time for namenode to startup to ensure the file system is not in safemode
MAX_CHECKS=10
CT=0
IN_SAFEMODE=$(su hdfs -l -s /bin/bash -c "hadoop dfsadmin -report | grep \"Safe mode\"")
CHECK_CODE=$?
while (true)
do
        if [ ! -z "$IN_SAFEMODE" ]; then
           echo "Waiting for cluser to exit safe mode...."
           sleep 1m
           IN_SAFEMODE=$(su hdfs -l -s /bin/bash -c "hadoop dfsadmin -report | grep \"Safe mode\"")
			CHECK_CODE=$?		
        else
        	if [ $CHECK_CODE -ne 0 ]; then
        		echo "Cluster is still a bit unstable, waiting for cluster stability....."
				sleep 1m
				su hdfs -l -s /bin/bash -c "hadoop dfsadmin -report"
        		CHECK_CODE=$?
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

# Start Region Server Daemon
chmod a+x /etc/init.d/hadoop-hbase-regionserver
service hadoop-hbase-regionserver start