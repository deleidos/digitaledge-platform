#!/bin/bash


#
# This chkconfig: spec is for Centos only  (/etc/rc[0-6].d linkage)
# chkconfig: 2345 82 13
# description: Summary: RTWS Node Initialization 
# processname: N/A
# pidfile: N/A
### BEGIN INIT INFO
# Provides:          RTWS Init
# Required-Start:    $network $local_fs
# Required-Stop:
# Should-Start:      $named
# Should-Stop:
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: RTWS
### END INIT INFO

#
#
# This script is intended as an init.d hook to start/stop rtws services. For
# the start call, this script delegates to the rtws_start script, which pulls
# the initialization manifest from the configured S3 bucket an performs the
# scripted actions (except those labeled as shutdown actions). The shutdown
# actions found in the manifest are instead written to a temporary file for
# later use (/etc/.rtws.stop). The stop call of this script delegates to the
# rtws_stop script, which runs the scripted shutdown commands previously saved
# in the temp file.
# Note: The shutdown commands are saved into a temporary file because the
# manifest file changes over time. The correct shutdown sequence is that which
# was originally listed in the manifest used to start the node, which may have
# been overwritten since the node was started.

PATH=/sbin:/usr/sbin:/bin:/usr/bin

start() {
  if [ -x /usr/local/rtws/boot/rtws_start.sh ]; then
    /usr/local/rtws/boot/rtws_start.sh &> /var/log/rtws_start.log 2>&1
  fi
}

stop() {
  if [ -x /usr/local/rtws/boot/rtws_stop.sh ]; then
    /usr/local/rtws/boot/rtws_stop.sh > /var/log/rtws_stop.log 2>&1
  fi
}

case "$1" in
  start)
    start
    ;;
  restart)
    stop
    start
    ;;
  reload|force-reload)
    echo "Error: argument '$1' not supported" >&2
    exit 3
    ;;
  stop)
    stop
    ;;
  status)
    echo "Error: argument '$1' not supported" >&2
    exit 3
    ;;
  *)
    echo $"Usage: $0 {start|stop|restart|reload|status}" >&2
    exit 3
    ;;
esac
