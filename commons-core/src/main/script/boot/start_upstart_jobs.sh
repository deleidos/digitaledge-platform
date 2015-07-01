#!/bin/bash

INIT_DIR=/etc/init

if [ ! -d $INIT_DIR ]; then
  echo "Directory /etc/init doesn't exist"
  exit 1
fi

# Create the command-listener upstart job

if [ -f $INIT_DIR/command-listener.conf ]; then
  rm -f $INIT_DIR/command-listener.conf
fi

touch $INIT_DIR/command-listener.conf

cat >> $INIT_DIR/command-listener.conf << "EOF"
#
# This conf defines the command-listener upstart command. It is used
# to start the command listener server and respawn it when it terminates
# unexpectedly.  Log directory is located where the script is executed from,
# in this case EXECPATH/bin/boot/logs/.
#

description     "command listener server"

respawn

exec /usr/local/rtws/commons-core/bin/boot/start_command_listener.sh
EOF

start command-listener




# Create the jmx-server upstart job (if requested via existence of start script)
if [ -f /usr/local/rtws/jmx/bin/start_jmxserver.sh ]; then

cat >> $INIT_DIR/jmx-server.conf << "EOF"
#
# This conf defines the jmx server upstart command. It is used
# to start the jmx server and respawn it when it terminates
# unexpectedly.  Log directory is located where the script is executed from,
# in this case EXECPATH/logs/.
#

description     "jmx server"

respawn

exec /usr/local/rtws/jmx/bin/start_jmxserver.sh
EOF

start jmx-server
fi