#!/bin/bash
set -x

INSTALL_PREFIX=/usr/local

cd $INSTALL_PREFIX/biserver-ce/ ; ./stop-pentaho.sh

cd $INSTALL_PREFIX/administration-console/  ; ./stop-pac.sh