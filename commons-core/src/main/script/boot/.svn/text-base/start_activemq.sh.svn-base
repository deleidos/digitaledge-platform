#!/bin/bash
#
# Setups the activemq JVM heap size and MemoryUsage values, then starts activemq.
source /etc/rtwsrc

#65% of total memory will be used in activemq config,mem size in mb
totalMemory=`free -m | grep "^Mem" | awk '{print $2}'`
echo "Total memory available on the system $totalMemory"

#we give activeMQ 60% of total available memory
mqmem=60

size=`expr \( \( $totalMemory \* $mqmem \) / 100 \)`
# Ensure there is enough memory left over for JVM C (non-heap, non-permgen, non-codecache) memory and the OS
remaining=`expr $totalMemory - $size`
if [ $remaining -lt 768 ]; then
    swapon -a
    size=`expr $totalMemory - 768`
fi

echo "$mqmem% of total memory, available memory to be used in activemq $size"

#the 65% of the total memory in size will now be the 100% that we take memory from
#now take 43% of that memory for the memory usage size
memsize=`expr \( $size \* 43 \) / 100`

#jvmsize to use the size calculated
jvmsize=$size

echo "43% of memory available for activemq (MemoryUsage) $memsize"
echo "memory available for activemq (JVM Heap) $jvmsize"

echo "JVM availabe heap size $jvmsize"

#set activemq ENV var for activemq.xml to read
export MEMORY_USAGE="$memsize mb"
echo "activemq memoryusage size argument $memsize mb"

#Calculate volume usage size on the fly, take the size of attached/mounted volumes in the raid and then give 80% to activemq

#get total raid size for attached volumes
totalVolumeSize=`df -m | grep $RTWS_RAID_PATH | awk '{print $2}'`

echo "Total raid/volume size available on instance $totalVolumeSize"

#calculate activemq disk usage size, take 80% of total volume size
diskSize=`expr \( \( $totalVolumeSize \* 80 \) / 100 \) - 128`

#set environment variable, activemq config will read value and set for process
export DISK_USAGE="$diskSize mb"
echo "activemq diskusage size argument $diskSize"

# compute size of the eden+survivor space
newsize=`expr $size / 3`
echo "activemq eden+survivor size $newsize"

# compute size of the code cache
codesize=`expr \( \( $totalMemory \* 3 \) / 100 \)`
if [ $codesize -gt 192 ]; then
    codesize=128;
elif [ $codesize -gt 96 ]; then
    codesize=64;
else
    codesize=32;
fi

echo "Total memory for code cache: $codesize"

echo ACTIVEMQ_OPTS_MEMORY=\"-XX:+UseParallelOldGC -XX:-UseAdaptiveSizePolicy -XX:InitialCodeCacheSize=$codesize'm' -XX:ReservedCodeCacheSize=$codesize'm' -XX:NewSize=$newsize'm' -XX:MaxNewSize=$newsize'm' -XX:SurvivorRatio=2 -XX:PermSize=128m -XX:MaxPermSize=128m -Xms${jvmsize}M -Xmx${jvmsize}M\" > /etc/default/activemq

grep vm.overcommit_ratio /etc/sysctl.conf
if [ $? -ne 0 ]; then
	echo vm.overcommit_ratio=100 >> /etc/sysctl.conf
fi

grep vm.overcommit_memory /etc/sysctl.conf
if [ $? -ne 0 ]; then
	echo vm.overcommit_memory=2 >> /etc/sysctl.conf
fi

sysctl -p /etc/sysctl.conf

grep nofile /etc/security/limits.conf | grep root
if [ $? -ne 0 ]; then
  echo "root		 soft		nofile		20480" >> /etc/security/limits.conf
  echo "root		 hard 		nofile		20480" >> /etc/security/limits.conf
fi

grep nofile /etc/security/limits.conf | grep activemq
if [ $? -ne 0 ]; then
  echo "activemq		 soft		nofile		20480" >> /etc/security/limits.conf
  echo "activemq		 hard 		nofile		20480" >> /etc/security/limits.conf
fi

ulimit -n 20480

#START|activemq|/usr/local/apache-activemq/bin/activemq start
COMMAND="/usr/local/apache-activemq/bin/activemq start"
COUNTER=1

su activemq -c "$COMMAND" &
sleep 10

until [ $COUNTER -eq 4 ]; do
echo "Checking for activemq startup errors, if an error occurs it will try to correct the problem ..."

if [ $(grep ERROR /usr/local/apache-activemq/data/activemq.log | wc -l) -gt 0 ]; then
echo $(grep ERROR /usr/local/apache-activemq/data/activemq.log | wc -l)
if [ $COUNTER -eq 3 ]; then
#wipe khahabd
echo "Wiping Kahadb (this might take awhile)..."
sudo rm -rf /mnt/rdafs/activemq/kahadb/db.data
fi
echo "Attempting to start ActiveMq..."
cat /usr/local/apache-activemq/data/activemq.log >> /usr/local/apache-activemq/data/activemq_OLD.log
rm -rf /usr/local/apache-activemq/data/activemq.log
su activemq -c "$COMMAND" &
echo Attempt $COUNTER
fi

sleep 5

let COUNTER+=1

done
