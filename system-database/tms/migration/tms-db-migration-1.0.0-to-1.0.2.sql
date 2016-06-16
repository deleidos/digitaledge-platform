-- TODO: Change 1.0.2 to the tag version
INSERT INTO APPLICATION.SOFTWARE_RELEASES VALUES('1.0.2', 'The 1.0.2 software release version', '');

-- Change the 1.0.2 to the tag version and the ami ids if a new one was created
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-east-1', '1.0.2', 'ami-3c8d3255', 'ami-e88e3181');
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-west-1', '1.0.2', 'ami-3c8d3255', 'ami-e88e3181');
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-west-2', '1.0.2', 'ami-3c8d3255', 'ami-e88e3181');

-- Add Alert Controller to webapps config
INSERT INTO APPLICATION.WEBAPPS_CONFIG VALUES('com.deleidos.rtws.webapp.alertcontroller','basic','A Alerts Management Application.','com.deleidos.rtws.webapp.alertsapi','com.deleidos.rtws.ext.datasink.SaicAlertingDataSink');


-- Update Search application config
update APPLICATION.WEBAPPS_CONFIG SET DESCRIPTION='Search webapp to search data indexed by the LuceneIndexingDataSink', WEBAPPS_DEPENDENCIES='com.deleidos.rtws.webapp.searchapi', DATASINK_DEPENDENCIES='com.deleidos.rtws.ext.datasink.LuceneIndexingDataSink' WHERE FQN='com.deleidos.rtws.webapp.search';

--Update Public DNS name from - to . notation
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET PUBLIC_DOMAIN_NAME='default.@build-domain@' WHERE PROCESS_GROUP_NAME='webapps.default';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET PUBLIC_DOMAIN_NAME='default.@build-domain@' WHERE PROCESS_GROUP_NAME='webapps.main';

-- Commit
commit;
