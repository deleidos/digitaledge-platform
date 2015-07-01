#!/bin/bash
set -x

######################################################################
# This script is used to install cassandra on a ubuntu instance that #
# doesn't have the software burned in the ami/emi.                   #
######################################################################

source /etc/rtwsrc

INSTALLED=false
if [ -d /var/lib/cassandra ]; then
  INSTALLED=true
fi

if [ $INSTALLED == false ]; then
  echo "Installing Cassandra Server ..."
  if [ "$RTWS_CASSANDRA_SOURCE" == "DATASTAX" ]; then
    echo "Downloading distribution from DataStax ..."
    # TODO: Install the DataStax community edition of Cassandra
  else
    echo "Downloading distribution from Apache ..."

    echo "Updating Apt Repository ..."
    echo "deb http://www.apache.org/dist/cassandra/debian 12x main" >> /etc/apt/sources.list
    echo "deb-src http://www.apache.org/dist/cassandra/debian 12x main" >> /etc/apt/sources.list

    echo "Exporting GPG keys ..."
    gpg --keyserver pgp.mit.edu --recv-keys F758CE318D77295D
    gpg --export --armor F758CE318D77295D | sudo apt-key add -

    gpg --keyserver pgp.mit.edu --recv-keys 2B5C1B00
    gpg --export --armor 2B5C1B00 | sudo apt-key add -

    apt-get update
    echo "Y" | apt-get install cassandra

    # Give the server a little bit of time to settle on first start
    sleep 5
  fi
fi

echo "Stopping Cassandra Server ..."
service cassandra status
if [ $? == 0 ]; then
  service cassandra stop
fi

echo "Configuring Cassandra Server settings ..."
if [ ! -d /mnt/rdafs/cassandra ]; then
  echo "Creating Cassandra /mnt/rdafs directories ..."
  mkdir -p /mnt/rdafs/cassandra
  mkdir -p /mnt/rdafs/cassandra/saved_caches
  mkdir -p /mnt/rdafs/cassandra/data
  mkdir -p /mnt/rdafs/cassandra/commitlog
  mkdir -p /mnt/rdafs/cassandra/conf
  
  echo "Configuring Cassandra directories in /mnt/rdafs ..."
  chown -R cassandra:cassandra /mnt/rdafs/cassandra
  mv /etc/cassandra/* /mnt/rdafs/cassandra/conf
  rm -rf /etc/cassandra
  ln -s /mnt/rdafs/cassandra/conf /etc/cassandra

  echo "Configuring cassandra.yaml file  ..."
  sed -i 's/\/var\/lib\/cassandra\/saved_caches/\/mnt\/rdafs\/cassandra\/saved_caches/' /mnt/rdafs/cassandra/conf/cassandra.yaml
  sed -i 's/\/var\/lib\/cassandra\/commitlog/\/mnt\/rdafs\/cassandra\/commitlog/' /mnt/rdafs/cassandra/conf/cassandra.yaml
  sed -i 's/\/var\/lib\/cassandra\/data/\/mnt\/rdafs\/cassandra\/data/' /mnt/rdafs/cassandra/conf/cassandra.yaml
  cp /mnt/rdafs/cassandra/conf/cassandra.yaml /mnt/rdafs/cassandra/conf/cassandra.yaml.bak
    
  rm -rf /var/lib/cassandra/*
else
  rm -rf /etc/cassandra
  ln -s /mnt/rdafs/cassandra/conf /etc/cassandra
  rm -rf /var/lib/cassandra/*
  
  # Copying in the original copy because we need to ensure the seed node ip addresses are
  # set currently on system restart.
  echo "Replacing cassandra.yaml file ..."
  cp /mnt/rdafs/cassandra/conf/cassandra.yaml.bak /mnt/rdafs/cassandra/conf/cassandra.yaml
fi

echo "Configuring cluster settings  ..."
sed -i 's/Test Cluster/DigitalEdge Cluster/' /mnt/rdafs/cassandra/conf/cassandra.yaml
sed -i 's/# num_tokens: 256/num_tokens: 256/' /mnt/rdafs/cassandra/conf/cassandra.yaml

echo "Setting seed node ip addresses ..."
SELF_IP=`curl -s http://169.254.169.254/latest/meta-data/local-ipv4`
sed -i 's/listen_address: localhost/listen_address: '$SELF_IP'/' /mnt/rdafs/cassandra/conf/cassandra.yaml
sed -i 's/rpc_address: localhost/rpc_address: 0.0.0.0/' /mnt/rdafs/cassandra/conf/cassandra.yaml
sed -i 's/endpoint_snitch: SimpleSnitch/endpoint_snitch: Ec2Snitch/' /mnt/rdafs/cassandra/conf/cassandra.yaml
      
SEED1_IP=""
while [ -z $SEED1_IP ]; do
  SEED1_IP=`host cassandra-seednode1.$RTWS_DOMAIN | cut -d' ' -f4`
  sleep 1
done

SEED2_IP=""
while [ -z $SEED2_IP ]; do
  SEED2_IP=`host cassandra-seednode2.$RTWS_DOMAIN | cut -d' ' -f4`
  sleep 1
done

sed -i 's/seeds: "127.0.0.1"/seeds: "'$SEED1_IP,$SEED2_IP'"/' /mnt/rdafs/cassandra/conf/cassandra.yaml

echo "Configuring Resource Limits ..."
if [ -f /etc/security/limits.d/cassandra.conf ]; then
  mv /etc/security/limits.d/cassandra.conf /etc/security/limits.d/cassandra.conf.bak
fi

echo "cassandra - memlock unlimited" > /etc/security/limits.d/cassandra.conf
echo "cassandra - nofile 100000" >> /etc/security/limits.d/cassandra.conf
echo "cassandra - nproc 32768" >> /etc/security/limits.d/cassandra.conf
echo "cassandra - as unlimited" >> /etc/security/limits.d/cassandra.conf
# If running Cassandra as root in Ubuntu, it is required to set the limits for root explicitly
echo "root - memlock unlimited" >> /etc/security/limits.d/cassandra.conf
echo "root - nofile 100000" >> /etc/security/limits.d/cassandra.conf
echo "root - nproc 32768" >> /etc/security/limits.d/cassandra.conf
echo "root - as unlimited" >> /etc/security/limits.d/cassandra.conf

grep "vm.max_map_count" /etc/sysctl.conf
if [ $? != 0 ]; then
  echo "vm.max_map_count = 131072" >> /etc/sysctl.conf
fi

sysctl -p

# Disable swap to prevent JVM from responding poorly because it is buried in swap and ensures
# that the OS OutOfMemory (OOM) killer does not kill Cassandra.
swapoff --all

echo "Starting Cassandra Server ..."
chmod +x /etc/init.d/cassandra
service cassandra start

echo "Cassandra Server installation and configuration complete."
