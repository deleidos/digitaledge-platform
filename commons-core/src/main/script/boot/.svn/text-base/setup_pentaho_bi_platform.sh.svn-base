#!/bin/bash
set -x

INSTALL_PREFIX=/usr/local
# Customize (as needed) for this domain

perl -i -pe "s/console.start.port.number=8099/console.start.port.number=80/g" $INSTALL_PREFIX/administration-console/resource/config/console.properties

find $INSTALL_PREFIX/biserver-ce/ -name '*.sh' -exec chmod +x {} \;
find $INSTALL_PREFIX/administration-console/ -name '*.sh' -exec chmod +x {} \;

cp -fv /usr/local/rtws/commons-core/lib/h2*.jar $INSTALL_PREFIX/biserver-ce/tomcat/lib/.
cp -fv /usr/local/rtws/commons-core/lib/h2*.jar $INSTALL_PREFIX/administration-console/lib/.