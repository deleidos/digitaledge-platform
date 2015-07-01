#!/bin/bash
set -x

#Enables SSH on the Gateway AMI if the instance is started within a VPC

source /etc/rtwsrc

if [ "$RTWS_VPC_ENABLED" == "true" ]; then
	chmod 755 /usr/bin/ssh
else
	echo Not running in a VPC or VPC user data is not correct:  $RTWS_VPC_ENABLED
fi