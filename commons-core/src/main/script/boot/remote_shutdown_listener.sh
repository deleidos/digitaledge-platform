#!/bin/bash

source /etc/rtwsrc

java -DRTWS_ROOT_LOG_LEVEL=$RTWS_ROOT_LOG_LEVEL -DRTWS_SAIC_LOG_LEVEL=$RTWS_SAIC_LOG_LEVEL -Xms56M -Xmx56M -cp /usr/local/rtws/commons-core/lib/*: com.saic.rtws.commons.net.shutdown.Shutdown &


# Ensure it say's up via crontab entry
# Nasty, but quick way to add to crontab entry 
if [ ! -f /var/spool/cron/crontabs/root ]; then
	echo "Adding watchdog to crontab entry for root"
	crontab -l
	echo "*/5 * * * * /usr/local/rtws/commons-core/bin/boot/remote_shutdown_listener_watchdog.sh" >> /var/spool/cron/crontabs/root
	crontab -l
fi
