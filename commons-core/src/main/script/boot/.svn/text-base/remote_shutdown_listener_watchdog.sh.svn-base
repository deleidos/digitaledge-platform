#!/bin/bash

if ! jps | grep -q Shutdown ; then
        echo "Shutdown Listener is not running, restarting it now...." | logger -t shutdown-wd
        /usr/local/rtws/commons-core/bin/boot/remote_shutdown_listener.sh
fi
