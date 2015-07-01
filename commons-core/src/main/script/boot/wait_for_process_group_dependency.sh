#!/bin/bash
#
# A utility to block until a process group dependency hostname can be resolved
#

source /etc/rtwsrc

# Block until a jobtracker pid can be resolved
FOUND=1
EXPECTED_RESOLVED_COUNT=3

for PROCESS_GROUP_NAME in $*
do
	RESOLVED_COUNT=0 # reset for each process group name requested
	while (( $FOUND != 0 ))
	do
			RESULT="$(nslookup $PROCESS_GROUP_NAME.$RTWS_DOMAIN | grep "server can't find")"

			if [ -z "$RESULT" ]; then
				echo "Resolved $PROCESS_GROUP_NAME.$RTWS_DOMAIN.  Result of dns lookup query: $RESULT"
				(( RESOLVED_COUNT += 1 ))
				
				# Wait until the hostname can be resolved at least N times				
				if (( $RESOLVED_COUNT >= $EXPECTED_RESOLVED_COUNT )); then					
					FOUND=0
				fi
			else
				echo "WARN: Unable to resolve $PROCESS_GROUP_NAME.$RTWS_DOMAIN. Result of dns lookup query: $RESULT"
				sleep 2m
			fi
	done
	
	FOUND=1 # reset
done