#!/bin/bash

GWDB_DIR=/mnt/rdafs/gwdb
if [ ! -d $GWDB_DIR ]; then
  mkdir $GWDB_DIR
  chown jetty:jetty $GWDB_DIR
  echo "Created directory $GWDB_DIR"
fi

cp=`echo /usr/local/rtws/commons-core/lib/h2* | tr -s ' ' ';'`
java -cp $cp org.h2.tools.RunScript -url jdbc:h2:tcp://127.0.0.1:8161/commondb -user sa -password <redacted> -script h2_create_tenant_example_tables.sql