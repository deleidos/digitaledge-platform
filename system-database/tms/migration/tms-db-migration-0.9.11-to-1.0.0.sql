-- TODO: Change 1.0.0 to the tag version
INSERT INTO APPLICATION.SOFTWARE_RELEASES VALUES('1.0.0', 'The 1.0.0 software release version', '');

-- Remove 32 BIT COLUMNS
ALTER TABLE APPLICATION.MACHINE_IMAGES DROP COLUMN IF EXISTS MI_32BIT_PERMANENT;
ALTER TABLE APPLICATION.MACHINE_IMAGES DROP COLUMN IF EXISTS MI_32BIT_INSTANCE;

UPDATE APPLICATION.INSTANCE_TYPES SET NUM_BITS = '64';

-- Change the 1.0.0 to the tag version and the ami ids if a new one was created
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-east-1', '1.0.0', 'ami-3c8d3255', 'ami-e88e3181');
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-west-1', '1.0.0', 'ami-3c8d3255', 'ami-e88e3181');
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-west-2', '1.0.0', 'ami-3c8d3255', 'ami-e88e3181');

-- Disable selection of alertviz & ingestapi by user for 1.0
DELETE FROM COMMONDB.APPLICATION.WEBAPPS_CONFIG WHERE FQN='com.deleidos.rtws.alertviz' or FQN='com.deleidos.rtws.webapp.ingestapi';

-- Disable selection of jmsdatasink & scriptingdatasink by user for 1.0
DELETE FROM APPLICATION.DATASINK_CONFIG WHERE FQN='com.deleidos.rtws.ext.datasink.ScriptingDataSink' or FQN='com.deleidos.rtws.ext.datasink.JMSDataSink';

-- Remove webapps.auth process group
DELETE FROM APPLICATION.DATASINK_CONFIG WHERE PROCESS_GROUP_NAME='webapps.auth';

-- Remove TRANSPORT_CONFIG table, no longer needed or used
DROP TABLE APPLICATION.TRANSPORT_CONFIG;

-- Remove column data from DATASINK_CONFIG that is now dynamically generated
UPDATE APPLICATION.DATASINK_CONFIG SET INGEST_CONFIG_FILENAME='';
UPDATE APPLICATION.DATASINK_CONFIG SET PIPELINE_XML_FILENAME='';
UPDATE APPLICATION.DATASINK_CONFIG SET PIPELINE_XML_TEMPLATE='';

-- Add new ExternalHbaseDataSink config info
INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG VALUES ('datasink.exthbase','datasink.default','ingest-exthbase?.@build.domain@','ingest.rtws.saic.com','','m1.large','instance','','datasink-exthbase.ini','services.exthbase.xml', '{"default-num-volumes" : 0,"default-volume-size" : 0,"config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true,  "config-scaling" : true,  "config-jms-persistence" : false }');
INSERT INTO APPLICATION.DATASINK_CONFIG VALUES ('com.deleidos.rtws.ext.datasink.ExternalHbaseDataSink', 'Y', 'Y', '2.0', '', '', '', 'datasink.exthbase');

-- Fix typo 
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET SECURITY_GROUPS = 'datasink.default' WHERE PROCESS_GROUP_NAME = 'datasink.script';

-- Remove pentaho
DELETE FROM APPLICATION.WEBAPPS_CONFIG WHERE FQN='processgroup:pentaho.bi.server';

-- Adjust volume number and sizes
update application.process_group_config set config_permissions=replace(config_permissions,'"default-num-volumes" : 4','"default-num-volumes" : 2') where config_permissions like '%"default-num-volumes" : 4%';
update application.process_group_config set config_permissions=replace(config_permissions,'"default-num-volumes" : 1','"default-num-volumes" : 2') where config_permissions like '%"default-num-volumes" : 1%';
update application.process_group_config set config_permissions=replace(config_permissions,'"default-volume-size" : 25','"default-volume-size" : 50') where process_group_name = 'hadoop.hbase.datanode.regionserver';
update application.process_group_config set config_permissions=replace(config_permissions,'"default-volume-size" : 25','"default-volume-size" : 15') where config_permissions like '%"default-volume-size" : 25%';
update application.process_group_config set config_permissions=replace(config_permissions,'"default-volume-size" : 200','"default-volume-size" : 50') where config_permissions like '%"default-volume-size" : 200%';
commit;
