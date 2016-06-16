-- TODO: Change x.x.x to the tag version
INSERT INTO APPLICATION.SOFTWARE_RELEASES VALUES('0.9.11', 'The 0.9.11 software release version', '');

-- Change the x.x.x to the tag version and the ami ids if a new one was created
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-east-1', '0.9.11', 'ami-b38d22da', 'ami-118d2278', 'ami-b38d22da', 'ami-118d2278');
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-west-1', '0.9.11', 'ami-b38d22da', 'ami-118d2278', 'ami-b38d22da', 'ami-118d2278');
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-west-2', '0.9.11', 'ami-b38d22da', 'ami-118d2278', 'ami-b38d22da', 'ami-118d2278');

-- Changes to alerts values
insert into "COMMONDB"."APPLICATION"."WEBAPPS_CONFIG" values('com.deleidos.rtws.webapp.alertsapi','basic','REST API for managing alert criteria used by the alerting data sink.','','com.deleidos.rtws.ext.datasink.SaicAlertingDataSink');
insert into "COMMONDB"."APPLICATION"."WEBAPPS_CONFIG" (FQN,TYPE,DESCRIPTION,WEBAPPS_DEPENDENCIES,DATASINK_DEPENDENCIES) VALUES ('com.deleidos.rtws.webapp.alertsapi','basic','REST API for managing alert criteria used by the alerting data sink.','','com.deleidos.rtws.ext.datasink.SaicAlertingDataSink');

--Update Public DNS name from - to . notation
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET PUBLIC_DOMAIN_NAME='auth.@build-domain@' WHERE PROCESS_GROUP_NAME='gateway';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET PUBLIC_DOMAIN_NAME='jms-ext.@build-domain@' WHERE PROCESS_GROUP_NAME='jms.external';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET PUBLIC_DOMAIN_NAME='auth.@build-domain@' WHERE PROCESS_GROUP_NAME='webapps.auth';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET PUBLIC_DOMAIN_NAME='search.@build-domain@' WHERE PROCESS_GROUP_NAME='webapps.search';

--Update RDMS connection dns hosts
UPDATE APPLICATION.TENANT_RDBMS_CONNECTIONS SET HOST='auth.tms-dev.rtsaic.com' WHERE TENANT_ID='tms-dev';
UPDATE APPLICATION.TENANT_RDBMS_CONNECTIONS SET HOST='auth.aws-dev.rtsaic.com' WHERE TENANT_ID='aws-dev';

-- Fix config for hive.metastore 
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{"default-num-volumes" : 4, "default-volume-size" : 25, "config-volume-size" : true,  "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : false, "config-scaling" : false, "config-jms-persistence" : false }' where process_group_name = 'hive.metastore';

-- Disable selection of alertviz & ingestapi by user for 1.0
DELETE FROM COMMONDB.APPLICATION.WEBAPPS_CONFIG WHERE FQN='com.deleidos.rtws.alertviz' or FQN='com.deleidos.rtws.webapp.ingestapi';

-- Remove credentials from IAAS acocunts
ALTER TABLE APPLICATION.IAAS_ACCOUNTS DROP COLUMN IAAS_CREDENTIALS;
