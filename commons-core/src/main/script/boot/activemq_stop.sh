#!/bin/bash
#
# Sets the JavaHeap size for the activemq process before executing the stop command.

export ACTIVEMQ_OPTS="-Xms64m -Xmx64M"

if [ -x /usr/local/apache-activemq/bin/activemq ]; then
    /usr/local/apache-activemq/bin/activemq stop
fi
