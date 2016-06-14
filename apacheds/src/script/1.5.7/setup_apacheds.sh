#!/bin/bash

. /etc/rtwsrc

if [ $# -ne 1 ]; then
    echo "Usage: setup_apacheds <input-file>"
    exit 1
fi

pid=`ps auxwww | pgrep -f org.apache.directory.server.UberjarMain`

if [ -z "$pid" ]; then
  echo "ApacheDS is currently not running"
  exit 1
fi

while [ 1 ]
do
  netstat -an | grep -i LISTEN | grep 10389

  if [ $? -eq 0 ]; then
    break
  else
    if [ "$wait" == "......" ]; then
      echo "ApacheDS is not listening on port 10389"
      exit 1
    else
      wait="$wait""."
      sleep 5
    fi
  fi
done

counter=0

while [ $counter -le 15 ]
do
  records=$(nslookup $RTWS_FQDN | grep -A1 'Name:' | grep Address | awk -F': ' '{print $2}')
  if [ -z $records ]
  then
    echo "Trying to resolve $RTWS_FQDN"
    counter=$(( $counter + 1))
    sleep 60
  else
        echo "$records"
        break
  fi
done


#
# Change the Directory Manager admin password
#
java -Dlog4j.configuration=null -jar bin/deleidos-rtws-apacheds.jar changeSystemPassword > logs/change_apacheds_password.log 2>&1

#
# Load the LDIF file specified on command line arg
#
java -Dlog4j.configuration=file:conf/log4j.properties -jar bin/apacheds-tools.jar import -e -w XXXXXX -f $1 > logs/setup_apacheds.log 2>&1

#
# Change the Domain of the LDAP for backwards compatibility.
# Note: This will only run if the new domain does not already exist.
#
java -Dlog4j.configuration=file:conf/log4j.properties -jar bin/deleidos-rtws-apacheds.jar changeLdapDomain rtsaic deleidos > logs/change_apacheds_domain.log 2>&1