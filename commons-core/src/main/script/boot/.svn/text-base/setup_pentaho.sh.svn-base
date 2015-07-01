#!/bin/bash

RTWS_PROPERTIES_FILE=/usr/local/rtws/properties/rtws-common.properties
COMMONS_CORE_LIB_DIR=/usr/local/rtws/commons-core/lib/
# Get the DB connection information from the properties
DB_PASSWORD=$(cat $RTWS_PROPERTIES_FILE | grep h2.sa.connection.password | cut -d '=' -f 2- | cut -d '(' -f2 | cut -d ')' -f1)


# Decrypt the password
COMMONS_CONFIG_JAR=$COMMONS_CORE_LIB_DIR/$(ls $COMMONS_CORE_LIB_DIR | grep saic-rtws-commons-config-.*.jar)
DB_PASSWORD=$(<redacted>)

# Run pentaho repo setup
java -cp /usr/local/rtws/commons-core/lib/h2*.jar org.h2.tools.RunScript -url jdbc:h2:tcp://127.0.0.1:8161/commondb -user SA -password $DB_PASSWORD -script /usr/local/rtws/commons-core/bin/boot/h2_create_pentaho_repository.sql
