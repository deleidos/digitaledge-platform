#!/bin/bash
#
# Expected AWS userdata parameters.
# RTWS_DNS_ADDRESS The IP address of the local DNS server.
source /etc/rtwsrc

# If a parameter was passed, use it to override the configured value.
override=$1
if [ -n "$override" ]; then
  RTWS_DNS_ADDRESS=$override
fi

if [ ! -n "$RTWS_DNS_ADDRESS" ]; then
  echo "RTWS_DNS_ADDRESS is not set, skipping dns setup"
  exit 0
fi

# If adding a public dns server need to replace euca dns server to avoid a circular dependency
# Eventually need to find a way to remove old one ( this will just add the public dns server )
if [ -n "$RTWS_CUSTOMER_DNS_SERVER_IP" ]; then  
  echo "prepend domain-name-servers 127.0.0.1;" >> /etc/dhcp3/dhclient.conf
  echo "prepend domain-name-servers $RTWS_CUSTOMER_DNS_SERVER_IP;" >> /etc/dhcp3/dhclient.conf
fi

# Modify the DHCP configuration so that DHCP updated will also
# prepend the local DNS server into the configuration.
echo "prepend domain-name-servers $RTWS_DNS_ADDRESS;" >> /etc/dhcp3/dhclient.conf

# Restart the DHCP daemon so it recognizes the new conf file.
/etc/init.d/networking restart

# Do not modify resolv.conf after this script runs... (only on GW and systems)
if [ "$RTWS_IS_TMS" != "true" ] &&
   [[ "$RTWS_STORAGE_ENDPOINT" != *amazonaws* ]]; then
	chattr +i /etc/resolv.conf
fi

# Restart bind
sudo service bind9 restart
