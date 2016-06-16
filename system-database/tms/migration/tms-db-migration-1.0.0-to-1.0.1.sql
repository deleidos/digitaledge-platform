-- TODO: Change 1.0.1 to the tag version
INSERT INTO APPLICATION.SOFTWARE_RELEASES VALUES('1.0.1', 'The 1.0.1 software release version', '');

-- Change the 1.0.1 to the tag version and the ami ids if a new one was created
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-east-1', '1.0.1', 'ami-3c8d3255', 'ami-e88e3181');
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-west-1', '1.0.1', 'ami-3c8d3255', 'ami-e88e3181');
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-west-2', '1.0.1', 'ami-3c8d3255', 'ami-e88e3181');

-- Add Alert Controller to webapps config
INSERT INTO APPLICATION.WEBAPPS_CONFIG VALUES('com.deleidos.rtws.webapp.alertcontroller','basic','A Alerts Management Application.','com.deleidos.rtws.webapp.alertsapi','com.deleidos.rtws.ext.datasink.SaicAlertingDataSink');

--Update Public DNS name from - to . notation
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET PUBLIC_DOMAIN_NAME='default.@build-domain@' WHERE PROCESS_GROUP_NAME='webapps.default';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET PUBLIC_DOMAIN_NAME='default.@build-domain@' WHERE PROCESS_GROUP_NAME='webapps.main';

-- Commit
commit;
