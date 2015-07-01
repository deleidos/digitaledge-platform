#!/bin/bash
set -x

source /etc/rtwsrc

# Changes the hostname of the instance to be the RTWS_FQDN 

if [[ "$(whoami)" != "root" ]]; then
     echo "ERROR: You must be root to proceed..."
     exit 1
fi

echo "Determining hostname from userdata...."
PREFIX="$(echo $RTWS_FQDN | sed "s/$RTWS_DOMAIN//g")"
hstname=$(echo "${PREFIX%?}")
echo "Updating hostname to $hstname"
hostname $hstname
echo $hstname > /etc/hostname

echo "Setting the domain name to $RTWS_DOMAIN"
domainname $RTWS_DOMAIN

# Add an entry to /etc/hosts for this host until binding kicks in which happens by master at a later time
echo "$(curl -s -f http://169.254.169.254/latest/meta-data/local-ipv4) $hstname $RTWS_FQDN" >> /etc/hosts 

# Modify the setting of the domain-name to match expected setup
echo "Updating domain-name in dhclient.conf for Euca"     	
su root -l -s /bin/bash -c "source /etc/rtwsrc ; echo 'prepend domain-name \"$RTWS_DOMAIN \";' >> /etc/dhcp3/dhclient.conf"
cat /etc/dhcp3/dhclient.conf

# Restart the DHCP daemon so it recognizes the new conf file.
/etc/init.d/networking restart