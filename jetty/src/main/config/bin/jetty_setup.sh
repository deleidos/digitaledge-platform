#!/bin/sh

export JETTY_HOME=/usr/local/jetty
export JETTY_USER=root
export JETTY_PORT=80
export TMPDIR=/tmp

# Copy in the right libsetuid.so file so the setuid operation works
if [ `uname -m` = "x86_64" ] ; then
    echo "jetty_setup.sh: 64 bit architecture"
    if [ -e $JETTY_HOME/lib/ext/libsetuid64.so ] ; then
        cp $JETTY_HOME/lib/ext/libsetuid64.so $JETTY_HOME/lib/ext/libsetuid.so
    fi
else
    echo "jetty_setup.sh: 32 bit architecture"
    if [ -e $JETTY_HOME/lib/ext/libsetuid32.so ] ; then
        cp $JETTY_HOME/lib/ext/libsetuid32.so $JETTY_HOME/lib/ext/libsetuid.so
    fi
fi

grep nofile /etc/security/limits.conf | grep root
if [ $? -ne 0 ]; then
  echo "root		 soft		nofile		20480" >> /etc/security/limits.conf
  echo "root		 hard 		nofile		20480" >> /etc/security/limits.conf
fi

grep nofile /etc/security/limits.conf | grep jetty
if [ $? -ne 0 ]; then
  echo "jetty		 soft		nofile		20480" >> /etc/security/limits.conf
  echo "jetty		 hard 		nofile		20480" >> /etc/security/limits.conf
fi

if [ -f /etc/lsb-release ]; then
	DOS2UNIX="fromdos"
elif [ -f /etc/redhat-release ]; then
	DOS2UNIX="dos2unix"
fi

sudo chmod 644 $JETTY_HOME/start_template.ini
sudo chown jetty:jetty $JETTY_HOME/start_template.ini
sudo $DOS2UNIX $JETTY_HOME/start_template.ini
