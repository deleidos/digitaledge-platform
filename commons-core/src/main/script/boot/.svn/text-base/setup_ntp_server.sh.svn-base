#!/bin/bash
#
# Expected AWS userdata parameters.
# (none)

if [[ "$(whoami)" != "root" ]]; then
     echo "ERROR: You must be root to proceed..."
     exit -1
fi

#check if user data contains ntp server list
set -x

ntpServers=`cat /usr/local/rtws/properties/rtws-common.properties | grep ntp.server | cut -f2 -d"="`

if [ -z "$ntpServers" ]; then
  # No NTP user data was passed to instance do not setup ntp, exit
  echo "No RTWS_NTP_SERVER param found in /etc/rtwsrc, not setting up NTP server, exiting."

  #stop current service running with default ntp server installed in config
  service ntp stop
  exit 0
fi

#get ntp servers, list one per line
ntpServers=`echo "$ntpServers" | tr '\,' ' ' | tr -s [:space:] '\n'`
#ntpServers=`cat /etc/rtwsrc | grep RTWS_NTP_SERVER | awk -F"=" '{print $2}' | sed "s/,//g" | tr -s '[:space:]' '\n'`
echo "$ntpServers"

#add iburst tag to prefered server, first in list
ntpSetup=`echo "$ntpServers" | awk '{if(NR==1){print $0,"iburst"}else{print }}'`
echo "$ntpSetup"

#add server to beginning of each ntp server entry
ntpSetup=`echo "$ntpSetup" | awk '{print "server ", $0}'`
echo "$ntpSetup"

#update ntp.conf file, with global replace of default config
perl -i -pe "s/server ntp.ubuntu.com/$ntpSetup/g" /etc/ntp.conf

#restart ntp service with new configuration
service ntp restart