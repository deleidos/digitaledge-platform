#!/bin/bash
set -x

# Start it
service mongodb start
sleep 30
if [ $(grep /mnt/rdafs/mongodb/mongod.lock /var/log/mongodb/mongodb.log | wc -l) -gt 0 ]; then
echo "Bad shutdown, removing lock file and restarting mongo"
sudo rm -rf /mnt/rdafs/mongodb/mongod.lock
cat /var/log/mongodb/mongodb.log >> /var/log/mongodb/mongodb_OLD.log
rm -rf mongodb.log
service mongodb start
fi