UPDATE APPLICATION.PROCESS_GROUP_CONFIG 
SET CONFIG_PERMISSIONS='{"default-num-volumes" : 0, "default-volume-size" : 0, "config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true, "config-scaling" : true, "config-jms-persistence" : false }'
WHERE PROCESS_GROUP_NAME='datasink.sleep';

UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET DEFAULT_INSTANCE_TYPE='m1.medium' WHERE PROCESS_GROUP_NAME='datasink.exthbase' or PROCESS_GROUP_NAME='datasink.hbase';

INSERT INTO "APPLICATION"."WEBAPPS_CONFIG" (FQN,TYPE,DESCRIPTION,WEBAPPS_DEPENDENCIES,DATASINK_DEPENDENCIES) VALUES ('com.deleidos.rtws.webapp.phoenixapi','basic','Beta: REST API for interacting with the Phoenix SQL Library Embedded in HBase.','','');

INSERT INTO APPLICATION.INSTANCE_TYPES (INSTANCE_TYPE,NUM_BITS,NUM_CORES,MEMORY_MB) VALUES ('m3.medium',64,4,3840);
INSERT INTO APPLICATION.INSTANCE_TYPES (INSTANCE_TYPE,NUM_BITS,NUM_CORES,MEMORY_MB) VALUES ('m3.large',64,6.5,7680);
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET DEFAULT_INSTANCE_TYPE='m3.medium' WHERE PROCESS_GROUP_NAME='datasink.hbase';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET DEFAULT_INSTANCE_TYPE='m3.large' WHERE PROCESS_GROUP_NAME='hue.server' or PROCESS_GROUP_NAME like '%hadoop%';

INSERT INTO APPLICATION.WEBAPPS_CONFIG (FQN,TYPE,DESCRIPTION,WEBAPPS_DEPENDENCIES,DATASINK_DEPENDENCIES) VALUES ('processgroup:pentaho.bi.server','server','Pentaho BI Community Edition (CE) Suite','','');

INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('cassandra.datanode','datasink.default','private','cassandra-datanode?.@build.domain@','ingest.rtws.saic.com,jmxhost.external.rtws.saic.com',null,'m1.large','instance',null,'cassandra-node.ini',null,'{"default-num-volumes" : 2, "default-volume-size" : 50, "config-volume-size" : true, "config-persistent-ip" : false, "config-instance-size" : true, "config-min-max" : true, "config-scaling" : false, "config-jms-persistence" : false }');
INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('cassandra.seednode','datasink.default','private','cassandra-seednode?.@build.domain@','ingest.rtws.saic.com,jmxhost.external.rtws.saic.com',null,'m1.large','instance',null,'cassandra-node.ini',null,'{"default-num-volumes" : 2, "default-volume-size" : 50, "config-volume-size" : true, "config-persistent-ip" : false, "config-instance-size" : true, "config-min-max" : true, "config-scaling" : false, "config-jms-persistence" : false }');
INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('datasink.cassandra','datasink.default','private','cassandra-writer-node?.@build.domain@','ingest.rtws.saic.com,jmxhost.external.rtws.saic.com',null,'m1.small','instance',null,'datasink-cassandra.ini','services.cassandra.xml','{"default-num-volumes" : 0, "default-volume-size" : 0, "config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true, "config-scaling" : false, "config-jms-persistence" : false }');
INSERT INTO "APPLICATION"."DATASINK_CONFIG" (FQN,CAN_AUTOSCALE,USES_BLOCK_STORAGE,SCALE_UP_FACTOR,INGEST_CONFIG_FILENAME,PIPELINE_XML_FILENAME,PIPELINE_XML_TEMPLATE,PROCESS_GROUP_DEPENDENCIES) VALUES ('com.saic.cassandra.datasink.CassandraDataSink','Y','N',1,'','','','cassandra.seednode,cassandra.datanode, datasink.cassandra');
INSERT INTO "APPLICATION"."DATASINK_CONFIG" (FQN,CAN_AUTOSCALE,USES_BLOCK_STORAGE,SCALE_UP_FACTOR,INGEST_CONFIG_FILENAME,PIPELINE_XML_FILENAME,PIPELINE_XML_TEMPLATE,PROCESS_GROUP_DEPENDENCIES) VALUES ('com.saic.rtws.ext.datasink.MongoDbReplicatedDataSink','N','Y',0,'','','','mongodb.replicated, datasink.mongodb.replicated');
UPDATE APPLICATION.AVAILABILITY_ZONES SET STORAGE_ENDPOINT='http://s3-us-west-1.amazonaws.com:80' WHERE IAAS_REGION='us-west-1';
UPDATE APPLICATION.AVAILABILITY_ZONES SET STORAGE_ENDPOINT='http://s3-us-west-2.amazonaws.com:80' WHERE IAAS_REGION='us-west-2';

-- Commit
commit;