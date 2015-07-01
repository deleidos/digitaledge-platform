#!/bin/bash
#
# This script is used to startup the h2 database server
#

DATA_DIR=$1
IGNORE_SCHEMAS=$2

if [ $# -lt 1 ] ; then
    echo 'run_h2.sh: Missing data directory parameter';
    exit 1;
fi

cp=`echo /usr/local/rtws/commons-core/lib/h2* | tr -s ' ' ';'`

if [ -n "$H2DRIVERS" ] ; then
  cp="$cp:$H2DRIVERS"
fi

if [ -n "$CLASSPATH" ] ; then
  cp="$cp:$CLASSPATH"
fi

cp="$cp:/usr/local/rtws/commons-core/lib/DatabaseWrapper.jar:/usr/local/rtws/commons-core/lib/saic-rtws-commons-core.jar"
#echo "CLASSPATH: $cp"

echo "Configuring file descriptor limits ..."
grep nofile /etc/security/limits.conf | grep root
if [ $? -ne 0 ]; then
  echo "root		 soft		nofile		20480" >> /etc/security/limits.conf
  echo "root		 hard 		nofile		20480" >> /etc/security/limits.conf
fi

grep nofile /etc/security/limits.conf | grep rtws
if [ $? -ne 0 ]; then
  echo "rtws		 soft		nofile		20480" >> /etc/security/limits.conf
  echo "rtws		 hard 		nofile		20480" >> /etc/security/limits.conf
fi

ulimit -n 20480

totalMemory=`free -m | grep "^Mem" | awk '{print $2}'`
h2Memory=`expr \( $totalMemory \* 50 \) / 100`
letterM=m
javaMemStart=$h2Memory$letterM
javaMemMax=$h2Memory$letterM
firstboot=0

if [ ! -d "$DATA_DIR" ] ; then
    mkdir $DATA_DIR
    chown rtws:rtws $DATA_DIR
    chmod 0755 $DATA_DIR
fi

MOUNT_DIR="$DATA_DIR/h2db"
if [ ! -d "$MOUNT_DIR" ] ; then
    firstboot=1
    mkdir -p $MOUNT_DIR
    chown rtws:rtws $MOUNT_DIR
    chmod 0700 $MOUNT_DIR
fi

LOG_DIR="/usr/local/rtws/h2logs"
if [ ! -d "$LOG_DIR" ] ; then
    mkdir -p $LOG_DIR
    chown rtws:rtws $LOG_DIR
fi

#COMMAND="java -server -Xms$javaMemStart -Dh2.serverResultSetFetchSize=1000 -Xmx$javaMemMax -cp $cp org.h2.tools.Server -tcpPort 8161 -tcpAllowOthers -baseDir $MOUNT_DIR"
COMMAND="java -server -Xms$javaMemStart -Dh2.serverResultSetFetchSize=1000 -Xmx$javaMemMax -cp $cp com.saic.rtws.db.Wrapper -tcpPort 8161 -tcpAllowOthers -baseDir $MOUNT_DIR"

# java -cp "$cp" org.h2.tools.Server $@
su rtws -c "$COMMAND" > $LOG_DIR/h2console.log 2>&1 &

echo "Waiting for database ..."
while [ 1 ]
do
    netstat -an | grep -i LISTEN | grep 8161
    
    if [ $? -eq 0 ]; then
        break
    else
        if [ "$wait" == "......" ]; then
            echo "H2 Database is not listening on port 8161"
            exit 1
        else
            wait="$wait""."
            sleep 5
        fi
    fi
done

if [ $firstboot -eq 1  ] ; then
    if [ -z $IGNORE_SCHEMAS ]; then    	
    	# Add the schemas to the h2 database
    	echo "Creating schema ..."
    	java -cp $cp org.h2.tools.RunScript -url jdbc:h2:tcp://127.0.0.1:8161/commondb -user sa -script h2_create_schemas.sql
    
    	# Add users to the h2 database
    	echo "Creating users ..."
    	java -cp $cp org.h2.tools.RunScript -url jdbc:h2:tcp://127.0.0.1:8161/commondb -user sa -script h2_create_users.sql
    fi
fi

GW_USER_DATA=RTWS_IS_GATEWAY
GW_FLAG=`grep $GW_USER_DATA /etc/rtwsrc | cut -d'=' -f2`
if [[ ! -z "$GW_FLAG" && $GW_FLAG == "true" ]]; then
    echo "Configuring gateway's h2 database settings ..."
    java -cp $cp org.h2.tools.RunScript -url jdbc:h2:tcp://127.0.0.1:8161/commondb -user sa -password <redacted> -script h2_configure_settings.sql
fi
