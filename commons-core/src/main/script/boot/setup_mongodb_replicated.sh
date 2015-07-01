#!/bin/bash
set -x

# Set's up a single instance mongo db server using 10gen repo

source /etc/rtwsrc

if [ ! -d /mnt/rdafs/mongodb ]; then
	mkdir -p /mnt/rdafs/mongodb ; chown mongodb:mongodb /mnt/rdafs/mongodb
fi

# Replication Set variables
NODES_IN_REPLICA_SET=3
INSTANCE_NUMBER=`echo $RTWS_FQDN | cut -d '=' -f 2 | cut -d '.' -f 1 | sed 's/\(.*[a-z]\)\(.*\)/\2/'`
REPLICATION_SET_NUMBER=$(((INSTANCE_NUMBER-1) / NODES_IN_REPLICA_SET))
REPLICATION_SET_NAME=rs$REPLICATION_SET_NUMBER
echo "replSet=$REPLICATION_SET_NAME" >> /etc/mongodb.conf

# Update the expected port if needed based on datasink config
CONFIG_PREFIX="$(echo $RTWS_INGEST_CONFIG | sed "s/\.xml//g" | sed "s/services.//g" )"

RTWS_INGEST_CONFIG_DEFAULT="/usr/local/rtws/ingest/conf/pipeline.$CONFIG_PREFIX.default.xml"

if [ -f $RTWS_INGEST_CONFIG_DEFAULT ]; then
	
	EXPECTED_PORT="$(cat $RTWS_INGEST_CONFIG_DEFAULT | awk -F'<mongo-server-port>' '{print $2}' | awk -F'</mongo-server-port>' '{print $1}' | tr -sd '\n' '')"

	if [ ! -z $EXPECTED_PORT ]; then		
		echo "Swaping the port mongo runs on based on datasink requested port ($EXPECTED_PORT)."
		perl -i -pe "s/^#port = 27017/port=$EXPECTED_PORT/g" /etc/mongodb.conf
	fi
fi
 
# Start it
chmod a+x /etc/init.d/mongodb
service mongodb start
sleep 30
if [ $(grep /mnt/rdafs/mongodb/mongod.lock /var/log/mongodb/mongodb.log | wc -l) -gt 0 ]; then
echo "Bad shutdown, removing lock file and restarting mongo"
sudo rm -rf /mnt/rdafs/mongodb/mongod.lock
cat /var/log/mongodb/mongodb.log >> /var/log/mongodb/mongodb_OLD.log
rm -rf mongodb.log
service mongodb start
sleep 30
fi

if [ -z $EXPECTED_PORT ]; then
	EXPECTED_PORT=27017
fi

CONFIG="{
   \"_id\" : \"<setname>\",
     \"members\" : [
         {\"_id\" : 0, \"host\" : \"<host>\"},
         {\"_id\" : 1, \"host\" : \"<host>\"},
         {\"_id\" : 2, \"host\" : \"<host>\"},
     ]
}"

CONFIG=`echo $CONFIG | perl -i -pe "s/<setname>/$REPLICATION_SET_NAME/"`

for ((i=$((INSTANCE_NUMBER - NODES_IN_REPLICA_SET + 1)); i<=$INSTANCE_NUMBER; i++))
do
   	MONGO_INSTANCE=`echo $RTWS_FQDN | cut -d '=' -f 2 | perl -i -pe "s/[0-9]+\./$i./"`
    CONFIG=`echo $CONFIG | perl -i -pe "s/<host>/$MONGO_INSTANCE:$EXPECTED_PORT/"`
done
echo $CONFIG

retval=`mongo --host $RTWS_FQDN --port $EXPECTED_PORT --eval "printjson(rs.initiate($CONFIG))"`
while [ true ]; do
  # ugly check of replica set status (need to rework)
  init=`echo $retval | grep "Config now saved locally" | wc -l`
  reinit=`echo $retval | grep "already initialized" | wc -l`
  if [ $init -eq 1 ] || [ $reinit -eq 1 ] ; then
    break;
  fi
  sleep 30
  retval=`mongo --host $RTWS_FQDN --port $EXPECTED_PORT --eval "printjson(rs.initiate($CONFIG))"`
done

