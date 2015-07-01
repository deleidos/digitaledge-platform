#!/bin/bash

#  Quickly add necessary stuff to turn a ubuntu box into a X-windows enabled server for remote connections.
#  Info taken/derived from the following 2 sites:
#   http://aws-musings.com/4-easy-steps-to-enable-remote-desktop-on-your-ubuntu-ec2-instance/
#   https://help.ubuntu.com/community/FreeNX


export DEBIAN_FRONTEND=noninteractive
sudo -E apt-get update
sudo -E apt-get install -y ubuntu-desktop
sudo add-apt-repository ppa:freenx-team
# freenx build for ubuntu 10.10 maverick doesn't exist, use 10.04 lucid build
if [ -e /etc/apt/sources.list.d/freenx-team-ppa-maverick.list ]; then
  sudo sed -i 's/maverick/lucid/g' /etc/apt/sources.list.d/freenx-team-ppa-maverick.list
fi
sudo apt-get update
sudo aptitude install -y freenx

# Bug workaround for
mkdir /tmp/nx_bug_1378450 ; cd /tmp/nx_bug_1378450 ; wget 'https://bugs.launchpad.net/freenx-server/+bug/576359/+attachment/1378450/+files/nxsetup.tar.gz'
tar -xvf /tmp/nx_bug_1378450/nxsetup.tar.gz
sudo cp /tmp/nx_bug_1378450/nxsetup /usr/lib/nx/nxsetup
echo "N" | sudo /usr/lib/nx/nxsetup --install
sudo perl -i -pe's/#ENABLE_PASSDB_AUTHENTICATION="0"/ENABLE_PASSDB_AUTHENTICATION="1"/g' /etc/nxserver/node.conf
sudo /etc/init.d/ssh restart


# Enabling the PASSDB database  (with dummy password: 9i8u7y6t5r)
sudo nxserver --adduser ubuntu
echo "9i8u7y6t5r" | sudo nxserver --passwd ubuntu
sudo service freenx-server restart


# Re-configure key  (if desired)
#sudo dpkg-reconfigure freenx-server
#sudo cp /var/lib/nxserver/home/.ssh/client.id_dsa.key ~/
