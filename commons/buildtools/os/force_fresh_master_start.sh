#!/bin/bash

# Dev Utility to force a master to start "fresh" :)

if [ "$(whoami)" != "root" ]; then
	echo "ERROR: you must be root to run this script.."
	exit 1
fi


perl -i -pe 's/prepend domain-name-servers 127.0.0.1;//g' /etc/dhcp3/dhclient.conf
perl -i -pe 's/nameserver 127.0.0.1//g' /etc/resolv.conf

rm -fv /etc/bind/named.conf.local
cat >> /etc/bind/named.conf.local <<"EOF"
//
// Do any local configuration here
//

// Consider adding the 1918 zones here, if they are not used in your
// organization
//include "/etc/bind/zones.rfc1918";
EOF
chown root:bind /etc/bind/named.conf.local ; chmod 644 /etc/bind/named.conf.local

cd /usr/local/jetty/bin/ ; ./jetty_stop.sh

if [ -z "$(grep RTWS_DOMAIN /etc/rtwsrc | grep tms)" ]; then
	rm -frv /mnt/rdafs/repository/repository /mnt/rdafs/repository/version/ /mnt/rdafs/repository/workspaces/
fi

rm -vrf /etc/rtwsrc /home/rtws/p* /home/rtws/.ssh/ /usr/local/rtws/master/logs/*