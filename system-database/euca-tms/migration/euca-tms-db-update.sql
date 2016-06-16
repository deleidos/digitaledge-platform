-- Adding TENANT_ID to MACHINE_IMAGES to support AMI per Tenant requirement
ALTER TABLE APPLICATION.MACHINE_IMAGES ADD TENANT_ID VARCHAR(255) DEFAULT NULL;
ALTER TABLE APPLICATION.MACHINE_IMAGES DROP CONSTRAINT IF EXISTS MI_NAMING;
ALTER TABLE APPLICATION.MACHINE_IMAGES ADD CONSTRAINT MI_NAMING UNIQUE (IAAS_SERVICE_NAME, IAAS_REGION, SW_VERSION_ID, TENANT_ID);

ALTER TABLE APPLICATION.REGISTRATION ADD ACCOUNT_ID NUMBER DEFAULT NULL;

CREATE TABLE IF NOT EXISTS APPLICATION.VPC_NETWORK(
	ACCOUNT_ID				NUMBER			NOT NULL,
	VPC_ID					VARCHAR2(64),
	INTERNET_GATEWAY_ID		VARCHAR2(64),
	PUBLIC_SUBNET_ID		VARCHAR2(64),
	
	CONSTRAINT VPC_ACCOUNT_ID_FK FOREIGN KEY(ACCOUNT_ID) REFERENCES APPLICATION.IAAS_ACCOUNTS(ACCOUNT_ID),
);

CREATE TABLE IF NOT EXISTS APPLICATION.AMI_CREATE_REQUESTS (
	ID 						INT AUTO_INCREMENT,
	REQUEST_ID				VARCHAR(255) NOT NULL,
	REQUEST_TYPE			VARCHAR(255) NOT NULL,
	REQUEST_TIMESTAMP		TIMESTAMP DEFAULT NULL,
	CANONICAL_ID			VARCHAR(255) NOT NULL,
	STACK_ID				VARCHAR(255) DEFAULT NULL,
	STATUS					VARCHAR(255) DEFAULT NULL,
	ATTEMPTS				INT DEFAULT 0,
	INFO					VARCHAR(255) DEFAULT NULL,
	AMI_ID					VARCHAR(255) DEFAULT NULL,
	EMAIL_ADDRESS			VARCHAR(255) DEFAULT NULL
);
GRANT ALL ON APPLICATION.AMI_CREATE_REQUESTS TO APPUSER;

ALTER TABLE APPLICATION.AMI_CREATE_REQUESTS ADD REQUEST_TYPE VARCHAR(255) BEFORE REQUEST_TIMESTAMP;
UPDATE APPLICATION.AMI_CREATE_REQUESTS SET REQUEST_TYPE='rtws_base';
ALTER TABLE APPLICATION.AMI_CREATE_REQUESTS ALTER COLUMN REQUEST_TYPE SET NOT NULL;
ALTER TABLE APPLICATION.AMI_CREATE_REQUESTS DROP COLUMN IF EXISTS ACCESS_KEY;
ALTER TABLE APPLICATION.AMI_CREATE_REQUESTS DROP COLUMN  IF EXISTS SECRET_KEY;
ALTER TABLE APPLICATION.AMI_CREATE_REQUESTS ADD COLUMN ATTEMPTS BEFORE INFO INT DEFAULT 0;
ALTER TABLE APPLICATION.AMI_CREATE_REQUESTS ADD COLUMN PACKAGE_FOR_DOWNLOAD BOOLEAN DEFAULT false;

ALTER TABLE APPLICATION.PROCESS_GROUP_CONFIG ADD COLUMN VPC_SUBNET VARCHAR2(32) BEFORE INTERNAL_DOMAIN_NAME;
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET VPC_SUBNET = 'private';

CREATE TABLE IF NOT EXISTS APPLICATION.SCHEDULED_TASKS(
	id long not null AUTO_INCREMENT primary key,
	system varchar(1024) not null,
	processGroup varchar(512) NOT NULL,
	numNodes integer NOT NULL,
	scriptName varchar(512) NOT NULL,
	arguments varchar(1024),
	triggerCron varchar(1024) NOT NULL
);
GRANT ALL ON APPLICATION.SCHEDULED_TASKS TO APPUSER;

alter table APPLICATION.SCHEDULED_TASKS ADD UNIQUE(SYSTEM,PROCESSGROUP,NUMNODES,SCRIPTNAME,ARGUMENTS,TRIGGERCRON);



ALTER TABLE APPLICATION.MACHINE_IMAGES DROP COLUMN IF EXISTS MI_64BIT_PERMANENT;
UPDATE COMMONDB.APPLICATION.PROCESS_GROUP_CONFIG set INSTANCE_STORAGE='instance';

UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET FIXED_WEBAPPS='includes searchapi', INGEST_CONFIG_FILENAME='services.zoie.xml' WHERE PROCESS_GROUP_NAME='datasink.lucene';

update APPLICATION.DATASINK_CONFIG set FQN='com.deleidos.rtws.ext.datasink.AlertingDataSink' where FQN='com.deleidos.rtws.ext.datasink.SaicAlertingDataSink';
update APPLICATION.DATASINK_CONFIG set FQN='com.deleidos.rtws.ext.datasink.ExternalScale2InsightDataSink' where FQN='com.deleidos.rtws.ext.datasink.ExternalSaicScale2InsightDataSink';
update APPLICATION.DATASINK_CONFIG set FQN='com.deleidos.rtws.ext.datasink.Scale2InsightDataSink' where FQN='com.deleidos.rtws.ext.datasink.SaicScale2InsightDataSink';
update APPLICATION.WEBAPPS_CONFIG set FQN='com.deleidos.rtws.ext.datasink.AlertingDataSink' where FQN='com.deleidos.rtws.ext.datasink.SaicAlertingDataSink';
update APPLICATION.WEBAPPS_CONFIG set DATASINK_DEPENDENCIES='com.deleidos.rtws.ext.datasink.AlertingDataSink' where DATASINK_DEPENDENCIES='com.deleidos.rtws.ext.datasink.SaicAlertingDataSink';

INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('pentaho.bi.server',					'datasink.default',	'public',	'pentaho-bi.@build.domain@',			'',														'',	'm3.large',	'instance',	'',	'pentaho-bi.ini',			'',										'{"default-num-volumes" : 2, "default-volume-size" : 15,  "config-volume-size" : true, "config-persistent-ip" : true, "config-instance-size" : true, "config-min-max" : false,  "config-scaling" : false,  "config-jms-persistence" : false }');
INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('hue.server',						'datasink.default',	'public',	'hue-server.@build.domain@',			'',														'',	'm3.large',	'instance',	'',	'cloudera-hue-v2.ini',		'',										'{"default-num-volumes" : 1, "default-volume-size" : 15,  "config-volume-size" : true, "config-persistent-ip" : true, "config-instance-size" : true, "config-min-max" : false,  "config-scaling" : false,  "config-jms-persistence" : false }');
INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('datasink.exthdfs',					'datasink.default',	'public',	'ingest-exthdfs?.@build.domain@',		'ingest.rtws.deleidos.com',								'',	'm3.large',	'instance',	'',	'datasink-exthdfs.ini',		'services.exthdfs.xml', 				'{"default-num-volumes" : 0, "default-volume-size" : 0,  "config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : true, "config-min-max" : true,  "config-scaling" : true,  "config-jms-persistence" : false }');
INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('datasink.elasticsearchdatasink',	'datasink.default',	'public',	'elasticSearch?.@build.domain@',		'ingest.rtws.saic.com',									'',	'm3.large',	'instance',	'',	'datasink-custom.ini',		'services.elasticSearchDatasink.xml',	'{"default-num-volumes" : 0, "default-volume-size" : 0, "config-volume-size" : true, "config-persistent-ip" : false, "config-instance-size" : true, "default-num-instances" : 1, "config-min-max" : true, "config-scaling" : true, "config-jms-persistence" : false }');
INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('elasticsearchstore',				'datasink.default',	'public',	'elasticSearchStore?.@build.domain@',	'ingest.rtws.saic.com',									'',	'm3.large',	'instance',	'',	'elasticsearch-storage.ini','',										'{"default-num-volumes" : 2, "default-volume-size" : 50, "config-volume-size" : true, "config-persistent-ip" : false, "config-instance-size" : true, "default-num-instances" : 1, "config-min-max" : true, "config-scaling" : true, "config-jms-persistence" : false }');
INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('datasink.titan',					'datasink.default',	'private',	'titan-writer-node?.@build.domain@',	'ingest.rtws.saic.com',									'',	'm3.medium',	'instance', '', 'datasink-titan.ini',		'services.titan.xml',					'{"default-num-volumes" : 0, "default-volume-size" : 0, "config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "default-num-instances" : 1, "config-min-max" : false, "config-scaling" : false, "config-jms-persistence" : false }');
INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('titan.cassandra.datanode',			'datasink.default',	'private',	'cassandra-datanode?.@build.domain@',	'ingest.rtws.saic.com,jmxhost.external.rtws.saic.com',	'',	'm3.large',	'instance', '', 'titan-node.ini',			'',										'{"default-num-volumes" : 1, "default-volume-size" : 100, "config-volume-size" : true, "config-persistent-ip" : false, "config-instance-size" : true, "default-num-instances" : 3, "config-min-max" : false, "config-scaling" : false, "config-jms-persistence" : false }');
INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('titan.cassandra.seednode',			'datasink.default',	'private',	'cassandra-seednode?.@build.domain@',	'ingest.rtws.saic.com,jmxhost.external.rtws.saic.com',	'',	'm3.large',	'instance', '', 'titan-node.ini',			'',										'{"default-num-volumes" : 1, "default-volume-size" : 100, "config-volume-size" : true, "config-persistent-ip" : false, "config-instance-size" : true, "default-num-instances" : 2, "config-min-max" : false, "config-scaling" : false, "config-jms-persistence" : false }');

INSERT INTO "APPLICATION"."WEBAPPS_CONFIG" (FQN,TYPE,DESCRIPTION,WEBAPPS_DEPENDENCIES,DATASINK_DEPENDENCIES) VALUES ('processgroup:pentaho.bi.server','server','Pentaho BI Suite Community Edition (CE) Suite','','');
INSERT INTO "APPLICATION"."WEBAPPS_CONFIG" (FQN,TYPE,DESCRIPTION,WEBAPPS_DEPENDENCIES,DATASINK_DEPENDENCIES) VALUES ('processgroup:hue.server','server','Hue Web Application for Apache Hadoop','','com.deleidos.rtws.ext.datasink.HiveDataSink');

UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET MANIFEST_FILENAME='cloudera-master-v2.ini' WHERE PROCESS_GROUP_NAME='hadoop.hbase.namenode-master';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET MANIFEST_FILENAME='cloudera-jobtracker-v2.ini' WHERE PROCESS_GROUP_NAME='hadoop.jobtracker';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET MANIFEST_FILENAME='cloudera-zookeeper-v2.ini' WHERE PROCESS_GROUP_NAME='zookeeper';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET MANIFEST_FILENAME='cloudera-datanode-regionserver-v2.ini' WHERE PROCESS_GROUP_NAME='hadoop.hbase.datanode.regionserver';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET MANIFEST_FILENAME='cloudera-hive-metastore-v2.ini' WHERE PROCESS_GROUP_NAME='hive.metastore';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET MANIFEST_FILENAME='cloudera-hive-v2.ini' WHERE PROCESS_GROUP_NAME='datasink.hive';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET MANIFEST_FILENAME='cloudera-hue-v2.ini' WHERE PROCESS_GROUP_NAME='hue.server';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET MANIFEST_FILENAME='datasink-hbase-v2.ini' WHERE PROCESS_GROUP_NAME='datasink.hbase';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET MANIFEST_FILENAME='datasink-s2i-v2.ini' WHERE PROCESS_GROUP_NAME='datasink.s2i';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET MANIFEST_FILENAME='datasink-s2i-v2.ini' WHERE PROCESS_GROUP_NAME='datasink.exts2i';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET MANIFEST_FILENAME='datasink-exthbase-v2.ini' WHERE PROCESS_GROUP_NAME='datasink.exthbase';

INSERT INTO "APPLICATION"."DATASINK_CONFIG" (FQN,CAN_AUTOSCALE,USES_BLOCK_STORAGE,SCALE_UP_FACTOR,INGEST_CONFIG_FILENAME,PIPELINE_XML_FILENAME,PIPELINE_XML_TEMPLATE,PROCESS_GROUP_DEPENDENCIES) VALUES ('com.deleidos.datasink.elasticsearch.ElasticSearchDataSink','Y','N',1.0,'','','','elasticsearchstore, datasink.elasticsearchdatasink');
INSERT INTO "APPLICATION"."DATASINK_CONFIG" (FQN,CAN_AUTOSCALE,USES_BLOCK_STORAGE,SCALE_UP_FACTOR,INGEST_CONFIG_FILENAME,PIPELINE_XML_FILENAME,PIPELINE_XML_TEMPLATE,PROCESS_GROUP_DEPENDENCIES) VALUES ('com.deleidos.rtws.ext.datasink.TitanDataSink','N','N',0.0,'','','','titan.cassandra.seednode,titan.cassandra.datanode, datasink.titan');
INSERT INTO "APPLICATION.DATASINK_CONFIG" (FQN,CAN_AUTOSCALE,USES_BLOCK_STORAGE,SCALE_UP_FACTOR,INGEST_CONFIG_FILENAME,PIPELINE_XML_FILENAME,PIPELINE_XML_TEMPLATE,PROCESS_GROUP_DEPENDENCIES) VALUES ('com.deleidos.rtws.ext.datasink.DimensionDataSink','N','N',0,'','','','datasink.dimension');
INSERT INTO "APPLICATION.PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('datasink.dimension','datasink.default','private','datasink-dimension?.@build.domain@','ingest.rtws.deleidos.com','','m3.medium','instance','','ingest.ini','services.dimensionSink.xml','{"default-num-volumes" : 0, "default-volume-size" : 0,  "config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true,  "config-scaling" : true,  "config-jms-persistence" : false }');

INSERT INTO APPLICATION.DATASINK_CONFIG (FQN,CAN_AUTOSCALE,USES_BLOCK_STORAGE,SCALE_UP_FACTOR,INGEST_CONFIG_FILENAME,PIPELINE_XML_FILENAME,PIPELINE_XML_TEMPLATE,PROCESS_GROUP_DEPENDENCIES) VALUES ('com.saic.rtws.ext.datasink.S3DataSink','Y','N',2,'','','','datasink.s3');
INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME,SECURITY_GROUP,VPC_SUBNET,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS) VALUES ('datasink.s3','datasink.default','public','datasink-s3?.@build.domain@','ingest.rtws.saic.com',null,'m3.large','instance',null,'ingest.ini','services.s3.xml','{"default-num-volumes" : 0, "default-volume-size" : 0, "config-volume-size" : true, "config-persistent-ip" : true, "config-instance-size" : true, "config-min-max" : true, "config-scaling" : true, "config-jms-persistence" : false }');

-- Update Scaling config for Dimension Data Sink
UPDATE APPLICATION.DATASINK_CONFIG SET CAN_AUTOSCALE='Y' WHERE FQN='com.deleidos.rtws.ext.datasink.DimensionDataSink';
UPDATE APPLICATION.DATASINK_CONFIG SET SCALE_UP_FACTOR=0.75 WHERE FQN='com.deleidos.rtws.ext.datasink.DimensionDataSink';

DROP TABLE IF EXISTS APPLICATION.AMI_CREATE_REQUESTS;

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

UPDATE APPLICATION.DATASINK_CONFIG SET NUM_THREADS=4 WHERE FQN='com.deleidos.rtws.ext.datasink.AlertingDataSink' or FQN='com.deleidos.cassandra.datasink.CassandraDataSink' or FQN='com.deleidos.rtws.ext.datasink.MongoDbClusterDataSink' or FQN='com.deleidos.rtws.ext.datasink.MongoDbDataSink' or FQN='com.deleidos.rtws.ext.datasink.MongoDbReplicatedDataSink' or FQN='com.deleidos.rtws.ext.datasink.SleepDataSink'     ;

UPDATE APPLICATION.DATASINK_CONFIG SET NUM_THREADS=1 WHERE FQN='com.deleidos.rtws.ext.datasink.JsonToJdbcDataSink' or FQN='com.deleidos.rtws.ext.datasink.NOAACurrentObservationsDataSink' or FQN='gov.usdot.cv.websocket.datasink.WebSocketDataSink' or FQN='com.deleidos.rtws.ext.datasink.TitanDataSink'   ;

ALTER TABLE APPLICATION.DATASINK_CONFIG DROP COLUMN INGEST_CONFIG_FILENAME;

ALTER TABLE APPLICATION.DATASINK_CONFIG DROP COLUMN PIPELINE_XML_FILENAME;

ALTER TABLE APPLICATION.DATASINK_CONFIG DROP COLUMN PIPELINE_XML_TEMPLATE;

UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET DEFAULT_INSTANCE_TYPE = 'm3.medium' where DEFAULT_INSTANCE_TYPE = 'm1.small';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET DEFAULT_INSTANCE_TYPE = 'm3.medium' where DEFAULT_INSTANCE_TYPE = 'c1.medium';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET DEFAULT_INSTANCE_TYPE = 'm3.large' where DEFAULT_INSTANCE_TYPE = 'm1.large';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET DEFAULT_INSTANCE_TYPE = 'm3.xlarge' where DEFAULT_INSTANCE_TYPE = 'm1.xlarge';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET DEFAULT_INSTANCE_TYPE = 'm3.xlarge' where DEFAULT_INSTANCE_TYPE = 'm2.xlarge';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET DEFAULT_INSTANCE_TYPE = 'c3.2xlarge' where DEFAULT_INSTANCE_TYPE = 'c1.xlarge';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET DEFAULT_INSTANCE_TYPE = 'm3.medium' where DEFAULT_INSTANCE_TYPE = 'm1.medium';

UPDATE APPLICATION.SYSTEM_SIZING SET JMS_INSTANCE_TYPE = 'm3.large' where JMS_INSTANCE_TYPE = 'm1.large';
UPDATE APPLICATION.SYSTEM_SIZING SET JMS_INSTANCE_TYPE = 'm3.medium' where JMS_INSTANCE_TYPE = 'm1.small';
UPDATE APPLICATION.SYSTEM_SIZING SET JMS_INSTANCE_TYPE = 'm3.xlarge' where JMS_INSTANCE_TYPE = 'm2.xlarge';
 
INSERT INTO "APPLICATION"."INSTANCE_TYPES" (INSTANCE_TYPE,NUM_BITS,NUM_CORES,MEMORY_MB) VALUES ('m3.xlarge',	64,	4,	15000);
INSERT INTO "APPLICATION"."INSTANCE_TYPES" (INSTANCE_TYPE,NUM_BITS,NUM_CORES,MEMORY_MB) VALUES ('m3.xlarge',		64,	2,	15000);
INSERT INTO "APPLICATION"."INSTANCE_TYPES" (INSTANCE_TYPE,NUM_BITS,NUM_CORES,MEMORY_MB) VALUES ('c3.2xlarge',	64, 8,	15000);

ALTER TABLE APPLICATION.IAAS_ACCOUNTS ADD COLUMN VPC_ONLY CHAR(1) NOT NULL DEFAULT 'Y';
-- Commit
commit;