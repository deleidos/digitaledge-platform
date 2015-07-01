#!/bin/bash

if [[ "$(whoami)" != "root" ]]; then
     echo "ERROR: You must be root to proceed..."
     exit 1
fi

if [ ! -z "$(grep RTWS_DOMAIN /etc/rtwsrc | grep tms)" ]; then
	echo "ERROR: This is not to be run on a TMS system
	exit 1
fi

cd /usr/local/jetty/bin/ ; ./jetty_stop.sh ; \
 rm -fr /mnt/rdafs/repository/repository /mnt/rdafs/repository/version/ /mnt/rdafs/repository/workspaces/ ; \
 rm -f /usr/local/jetty/logs/* ; ./jetty_start.sh