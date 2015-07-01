#!/bin/sh

#Copies the generated keystores/truststores to directory locations used by the software

# Copy generated ssl-keystore/truststore to etc under jetty
cd /usr/local/jetty/etc
rm -f ssl-keystore
cp /usr/local/rtws/boot-apps/.key/external-ssl-keystore .
cp /usr/local/rtws/boot-apps/.key/internal-ssl-keystore .
cp /usr/local/rtws/boot-apps/.key/ssl-truststore .
chown jetty:jetty external-ssl-keystore internal-ssl-keystore ssl-truststore

#Copy generated ssl-keystore/truststore to etc under activemq
cd /usr/local/apache-activemq/conf
rm -f ssl-keystore
cp /usr/local/rtws/boot-apps/.key/external-ssl-keystore .
cp /usr/local/rtws/boot-apps/.key/internal-ssl-keystore .
cp /usr/local/rtws/boot-apps/.key/ssl-truststore .
chown activemq:activemq external-ssl-keystore internal-ssl-keystore ssl-truststore
