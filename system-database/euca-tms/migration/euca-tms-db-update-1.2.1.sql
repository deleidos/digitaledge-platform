UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET MANIFEST_FILENAME='datasink-mongo.ini'  WHERE PROCESS_GROUP_NAME='datasink.mongodb';

INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('mongodb.standalone','datasink.default','private','mongodb.@build.domain@',null,null,'m1.large','instance',null,'mongo.ini',null,'{"default-num-volumes" : 1, "default-volume-size" : 100, "config-volume-size" : true, "config-persistent-ip" : false, "default-num-instances" : 1, "config-instance-size" : false, "config-min-max" : false, "config-scaling" : false, "config-jms-persistence" : false }');

INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('mongodb.standalone.shard','datasink.default','private','mongodb-shard?.@build.domain@',null,null,'m1.large','instance',null,'mongo.ini',null,'{"default-num-volumes" : 1, "default-volume-size" : 100, "config-volume-size" : true, "config-persistent-ip" : false, "default-num-instances" : 1, "config-instance-size" : true, "config-min-max" : true, "config-scaling" : true, "config-jms-persistence" : false }');

INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('mongodb.standalone.mongos','datasink.default','private','mongos.@build.domain@',null,null,'m1.large','instance',null,'mongos.ini',null,'{"default-num-volumes" : 0, "default-volume-size" : 0, "config-volume-size" : false, "config-persistent-ip" : true, "default-num-instances" : 1, "config-instance-size" : true, "config-min-max" : true, "config-scaling" : false, "config-jms-persistence" : false }');

UPDATE APPLICATION.DATASINK_CONFIG SET PROCESS_GROUP_DEPENDENCIES='mongodb.standalone, datasink.mongodb' WHERE FQN='com.deleidos.rtws.ext.datasink.MongoDbDataSink';

INSERT INTO "APPLICATION"."DATASINK_CONFIG" (FQN,CAN_AUTOSCALE,USES_BLOCK_STORAGE,SCALE_UP_FACTOR,INGEST_CONFIG_FILENAME,PIPELINE_XML_FILENAME,PIPELINE_XML_TEMPLATE,PROCESS_GROUP_DEPENDENCIES) VALUES ('com.deleidos.rtws.ext.datasink.MongoDbClusterDataSink','N','Y',0,'','','','mongodb.config, mongodb.standalone.mongos, mongodb.standalone.shard, datasink.mongodb.cluster');

UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{"default-num-volumes" : 0, "default-volume-size" : 0, "config-volume-size" : false, "config-persistent-ip" : false, "default-num-instances" : 1, "config-instance-size" : true, "config-min-max" : true, "config-scaling" : true, "config-jms-persistence" : false }' WHERE PROCESS_GROUP_NAME='datasink.mongodb';

UPDATE APPLICATION.DATASINK_CONFIG SET CAN_AUTOSCALE='Y', SCALE_UP_FACTOR=0.75  WHERE FQN='com.deleidos.rtws.ext.datasink.MongoDbDataSink';

INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('datasink.mongodb.cluster','datasink.default','public','mongodb-writer-node?.@build.domain@','ingest.rtws.deleidos.com',null,'m1.large','instance',null,'datasink-mongo.ini','services.mongo.xml','{"default-num-volumes" : 0, "default-volume-size" : 0, "config-volume-size" : false, "config-persistent-ip" : false, "default-num-instances" : 1, "config-instance-size" : true, "config-min-max" : true, "config-scaling" : true, "config-jms-persistence" : false }');

INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('datasink.mongodb.replicated','datasink.default','public','mongodb-writer-replicated?.@build.domain@','ingest.rtws.deleidos.com',null,'m1.large','instance',null,'datasink-mongo.ini','services.mongo.xml','{"default-num-volumes" : 1, "default-volume-size" : 100, "config-volume-size" : true, "config-persistent-ip" : false, "default-num-instances" : 1, "config-instance-size" : false, "config-min-max" : true, "config-scaling" : true, "config-jms-persistence" : false }');

INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('mongodb.config','datasink.default','private','mongodb-config?.@build.domain@',null,null,'m1.small','instance',null,'mongo-config-servers.ini',null,'{"default-num-volumes" : 1, "default-volume-size" : 30, "config-volume-size" : true, "config-persistent-ip" : false, "default-num-instances" : 3, "config-instance-size" : true, "config-min-max" : false, "config-scaling" : false, "config-jms-persistence" : false }');

INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('mongodb.replicated','internal.default','private','mongodb-replicated?.@build.domain@',null,null,'m1.large','instance',null,'mongo-db.ini','services.mongo.xml','{"default-num-volumes" : 1, "default-volume-size" : 100, "config-volume-size" : true, "config-persistent-ip" : false, "default-num-instances" : 3, "config-instance-size" : true, "config-min-max" : false, "config-scaling" : false, "config-jms-persistence" : false }');

DROP TABLE IF EXISTS APPLICATION.AMI_CREATE_REQUESTS;

CREATE TABLE IF NOT EXISTS APPLICATION.ADMIN_EMAIL_CONFIG (
    ACCOUNT_ID				DECIMAL			NOT NULL,
    SERVICE					VARCHAR(8)		NOT NULL,
    EMAIL_FROM				VARCHAR(254)	NOT NULL,
    HOST					VARCHAR(255),
    PORT					NUMBER,
    USER					VARCHAR(254),
    USER_PASSWORD			VARCHAR(254),
    IS_AUTH_REQUIRED		BOOLEAN,
    IS_TLS_REQUIRED			BOOLEAN,
    CONSTRAINT EMAIL_CONFIG_ACCOUNT_ID_FK FOREIGN KEY (ACCOUNT_ID) REFERENCES APPLICATION.IAAS_ACCOUNTS(ACCOUNT_ID)
);

CREATE TABLE IF NOT EXISTS APPLICATION.SYSTEM_USAGE (
	 ACCOUNT_ID						NUMBER NOT NULL,
	 WBS_CODE						VARCHAR(512),
	 DOMAIN							VARCHAR(512) NOT NULL,
	 SNAPSHOT				        TIMESTAMP NOT NULL,
	 ALLOCATED_CORES				NUMBER NOT NULL,	 
	 ALLOCATED_EBS					FLOAT NOT NULL DEFAULT 0.0,
	 FOREIGN KEY (ACCOUNT_ID) REFERENCES APPLICATION.IAAS_ACCOUNTS(ACCOUNT_ID),
	 PRIMARY KEY (SNAPSHOT,DOMAIN)
);

CREATE TABLE IF NOT EXISTS APPLICATION.SYSTEM_USAGE_SUMMARY (
	 ACCOUNT_ID						NUMBER NOT NULL,
	 WBS_CODE						VARCHAR(512),
	 DOMAIN							VARCHAR(512) NOT NULL,
	 DAY							DATE NOT NULL,
	 HOUR							NUMBER NOT NULL,
	 ALLOCATED_CORES				NUMBER NOT NULL,
	 ALLOCATED_EBS					FLOAT NOT NULL DEFAULT 0.0,
	 FOREIGN KEY (ACCOUNT_ID) REFERENCES APPLICATION.IAAS_ACCOUNTS(ACCOUNT_ID)
);

ALTER TABLE APPLICATION.PROCESS_GROUP_CONFIG ADD OWNER VARCHAR2(255) NOT NULL DEFAULT 'admin';

ALTER TABLE APPLICATION.PROCESS_GROUP_CONFIG ADD VISIBILITY VARCHAR2(8) NOT NULL DEFAULT 'PRIVATE';

UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET VISIBILITY = 'PUBLIC';

ALTER TABLE APPLICATION.PROCESS_GROUP_CONFIG DROP COLUMN VPC_SUBNET;

ALTER TABLE APPLICATION.PROCESS_GROUP_CONFIG ADD FOREIGN KEY (OWNER) REFERENCES APPLICATION.TENANT_ACCOUNT_ACCESS(TENANT_ID);

ALTER TABLE APPLICATION.PROCESS_GROUP_CONFIG DROP PRIMARY KEY;

ALTER TABLE APPLICATION.PROCESS_GROUP_CONFIG ADD PRIMARY KEY (PROCESS_GROUP_NAME,OWNER);

ALTER TABLE APPLICATION.DATASINK_CONFIG ADD NUM_THREADS NUMBER NOT NULL DEFAULT 2;

UPDATE APPLICATION.DATASINK_CONFIG SET NUM_THREADS=4 WHERE FQN='com.deleidos.rtws.ext.datasink.AlertingDataSink' or FQN='com.deleidos.cassandra.datasink.CassandraDataSink' or FQN='com.deleidos.rtws.ext.datasink.MongoDbClusterDataSink' or FQN='com.deleidos.rtws.ext.datasink.MongoDbDataSink' or FQN='com.deleidos.rtws.ext.datasink.MongoDbReplicatedDataSink' or FQN='com.deleidos.rtws.ext.datasink.SleepDataSink'      ;

UPDATE APPLICATION.DATASINK_CONFIG SET NUM_THREADS=1 WHERE FQN='com.deleidos.rtws.ext.datasink.JsonToJdbcDataSink' or FQN='com.deleidos.rtws.ext.datasink.NOAACurrentObservationsDataSink' or FQN='gov.usdot.cv.websocket.datasink.WebSocketDataSink'   ;

ALTER TABLE APPLICATION.DATASINK_CONFIG DROP COLUMN INGEST_CONFIG_FILENAME;

ALTER TABLE APPLICATION.DATASINK_CONFIG DROP COLUMN PIPELINE_XML_FILENAME;

ALTER TABLE APPLICATION.DATASINK_CONFIG DROP COLUMN PIPELINE_XML_TEMPLATE;

UPDATE TABLE APPLICATION.PROCESS_GROUP_CONFIG SET MANAGEMENT_INTERFACES='ingest.rtws.deleidos.com' WHERE MANAGEMENT_INTERFACES='ingest.rtws.saic.com';

DELETE FROM APPLICATION.DATASINK_CONFIG WHERE FQN LIKE '%LuceneIndexingDataSink%' OR FQN LIKE '%LuceneSummarizationDataSink%';

DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='datasink.lucene-sum' or PROCESS_GROUP_NAME= 'datasink.lucene';

DELETE FROM APPLICATION.WEBAPPS_CONFIG WHERE FQN LIKE '%searchapi' or FQN LIKE '%search';

ALTER TABLE APPLICATION.IAAS_ACCOUNTS ADD COLUMN VPC_ONLY CHAR(1) NOT NULL DEFAULT 'Y';

-- Commit
commit;