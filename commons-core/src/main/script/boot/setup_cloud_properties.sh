#!/bin/bash

# Sets up cloud related properties/configs

if [ ! -d /usr/local/rtws/properties ]; then
	mkdir -p /usr/local/rtws/properties ; chown -R rtws:rtws /usr/local/rtws/properties
fi

grep RTWS_SECRET_KEY /etc/rtwsrc | sed "s/RTWS_SECRET_KEY/secretKey/" > /usr/local/rtws/properties/aws.properties
grep RTWS_ACCESS_KEY /etc/rtwsrc | sed "s/RTWS_ACCESS_KEY/accessKey/" >> /usr/local/rtws/properties/aws.properties
chmod 444 /usr/local/rtws/properties/aws.properties

if [ ! -d /usr/local/rtws/conf ]; then
	mkdir -p /usr/local/rtws/conf ; chown -R rtws:rtws /usr/local/rtws/conf
fi

grep RTWS_SECRET_KEY /etc/rtwsrc | sed "s/RTWS_SECRET_KEY/secretKey/" > /usr/local/rtws/conf/aws.properties
grep RTWS_ACCESS_KEY /etc/rtwsrc | sed "s/RTWS_ACCESS_KEY/accessKey/" >> /usr/local/rtws/conf/aws.properties
chmod 444 /usr/local/rtws/conf/aws.properties