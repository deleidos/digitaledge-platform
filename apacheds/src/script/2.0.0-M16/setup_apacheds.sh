#!/bin/bash

. /etc/rtwsrc

if [ $# -ne 1 ]; then
    echo "Usage: $0 <input-file>"
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

# Wait until the DNS is updated with the correct values
counter=0
public_hostname=$(curl -s -f http://169.254.169.254/latest/meta-data/public-hostname)

while [ $counter -le 15 ]
do
  name=$(nslookup $RTWS_FQDN | grep 'Name:' | cut -s -f2)

  if [ -z $name ]
  then
    echo "Trying to resolve $RTWS_FQDN"
    counter=$(( $counter + 1))
    sleep 60
  else
    if [ "$name" == "$public_hostname" ]
    then
      echo "$name"
      break
    else
      echo "Trying to resolve $RTWS_FQDN"
      counter=$(( $counter + 1))
      sleep 60
    fi
  fi
done


#
# Change the Directory Manager admin password
#
java -Dlog4j.configuration=null -jar lib/deleidos-rtws-apacheds.jar changeSystemPassword > logs/change_apacheds_password.log 2>&1

#
# Reset all MD5 user passwords to use SHA-256 - REMOVE after all passwords have been converted
#
java -Dlog4j.configuration=null -cp "/usr/local/apacheds-2.0.0-M16/lib/*" com.deleidos.rtws.ldaputils.LdapModify resetMD5UserPasswords > logs/reset_md5_passwords.log 2>&1
cp logs/reset_md5_passwords.log /mnt/rdafs/reset_md5_passwords.`date +%s`.log

#
# Install ldap-utils to be able to use ldapmodify to load LDIFs to the LDAP
#
if [ -f /etc/lsb-release ]; then
	apt-get -y install ldap-utils --force-yes
fi
if [ -f /etc/centos-release ]; then
	yum -y install openldap-clients
fi
	

#
# Load the LDIF file specified on command line arg
#
RTWS_PROPERTIES_FILE="/usr/local/rtws/properties/rtws-common.properties"
LDAP_URL="$(cat $RTWS_PROPERTIES_FILE | grep ldap.provider.url | cut -d '=' -f 2- | tr -sd '\r' '' | tr -sd '\n' '')"
LDAP_PRINCIPAL=$(cat "$RTWS_PROPERTIES_FILE" | grep ldap.principal | cut -d '=' -f 2- | sed 's/|/,/' | tr -sd '\r' '' | tr -sd '\n' '')
LDAP_PASS=$(cat "$RTWS_PROPERTIES_FILE" | grep ldap.credentials | cut -d '=' -f 2- | cut -d '(' -f2 | cut -d ')' -f1)

APACHEDS_LIB_DIR="/usr/local/apacheds/lib/"
COMMONS_CONFIG_JAR="$APACHEDS_LIB_DIR"/$(ls "$APACHEDS_LIB_DIR" | grep deleidos-rtws-commons-config-.*.jar)

echo "***********************************************************************************************"
echo " NOTE: \"ERR_250_ENTRY_ALREADY_EXISTS\" is an acceptable error in this log and can be ignored!"
echo "***********************************************************************************************"

VAR=$RTWS_DOMAIN
if [ "$RTWS_IS_TMS" == "true" ]; then
  VAR=com.deleidos.rtws.commons.config.ConfigEncryptor
fi

LDAP_PASS=$(java -cp "$COMMONS_CONFIG_JAR" com.deleidos.rtws.commons.config.ConfigEncryptor -d "$VAR" "$LDAP_PASS")

ldapmodify -H "$LDAP_URL" -D "$LDAP_PRINCIPAL" -w "$LDAP_PASS" -a -c -f $1 > logs/setup_apacheds.log 2>&1

