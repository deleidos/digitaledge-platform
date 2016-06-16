#!/bin/sh

export JETTY_HOME=/usr/local/jetty
export JETTY_USER=root
export JETTY_PORT=80
export TMPDIR=/tmp

# Get child Pids of this instance of Jetty
if [ -f /var/run/jetty.pid ]
then
	C_PIDS="$(pgrep -P $(cat /var/run/jetty.pid) 2> /dev/null)"
fi

./jetty.sh stop

for p in $C_PIDS
do
	echo "Waiting for clean termination of child jetty pid {$p}...."
	x=1
	while [ $x -lt 10 ]
	do
   		straggler="$(ps u -p $p --no-heading 2> /dev/null)"
   		
   		if [ -z "$straggler" ]
   		then
   			# Pid is gone, so leave 
   			x=99
   		else
   			sleep 30
   		fi
    	
    	x=`expr $x + 1`
	done
done