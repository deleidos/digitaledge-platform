#!/bin/bash
set -x

INSTALL_PREFIX=/usr/local

MONGO_PREFIX=mongodb-writer-node1.
RTWS_DOMAIN=`cat /etc/rtwsrc | grep "RTWS_DOMAIN=.*" | cut -d '=' -f 2`
MONGO_DOMAIN="${MONGO_PREFIX}${RTWS_DOMAIN}"

cd $INSTALL_PREFIX/biserver-ce/pentaho-solutions/Enron ; perl -i -pe 's/MONGO_PLACEHOLDER/$MONGO_DOMAIN/g' kettle-test.ktr

cd $INSTALL_PREFIX/biserver-ce/ ; ./start-pentaho.sh

cd $INSTALL_PREFIX/administration-console/ ; ./start-pac.sh > server.stdout 2>&1 &