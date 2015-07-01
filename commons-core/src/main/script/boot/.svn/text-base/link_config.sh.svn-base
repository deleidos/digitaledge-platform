#!/bin/bash
#
# Expected AWS userdata parameters.
# RTWS_FQDN - The fully qualified domain name of this instance.
source /etc/rtwsrc

dir=$1
node=`echo $RTWS_FQDN | cut -d'.' -f1`

ln -s $dir/activemq-$node.xml $dir/activemq.xml
