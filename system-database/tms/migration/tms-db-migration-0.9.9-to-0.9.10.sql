-- TODO: Change x.x.x to the tag version
INSERT INTO APPLICATION.SOFTWARE_RELEASES VALUES('0.9.10', 'The 0.9.10 software release version', '');

-- Change the x.x.x to the tag version and the ami ids if a new one was created
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-east-1', '0.9.10', 'ami-b38d22da', 'ami-118d2278', 'ami-b38d22da', 'ami-118d2278');
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-west-1', '0.9.10', 'ami-b38d22da', 'ami-118d2278', 'ami-b38d22da', 'ami-118d2278');
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-west-2', '0.9.10', 'ami-b38d22da', 'ami-118d2278', 'ami-b38d22da', 'ami-118d2278');

-- Remove the Pentaho Data sink.  Add Pentaho process group support to webapps
DELETE FROM APPLICATION.DATASINK_CONFIG WHERE FQN='com.deleidos.rtws.ext.datasink.PentahoBiPlatformDataSink';
INSERT INTO APPLICATION.WEBAPPS_CONFIG (FQN,TYPE,DESCRIPTION,WEBAPPS_DEPENDENCIES,DATASINK_DEPENDENCIES) VALUES ('processgroup:pentaho.bi.server','server','Pentaho BI server webapp instance','','');

-- Fix Hbase Data sink process group dependencies
UPDATE APPLICATION.DATASINK_CONFIG SET PROCESS_GROUP_DEPENDENCIES='zookeeper, hadoop.hbase.namenode-master, hadoop.jobtracker, hadoop.hbase.datanode.regionserver, datasink.hbase' WHERE FQN='com.deleidos.rtws.ext.datasink.HbaseDataSink';

-- Fix Mongo Data sink process group dependencies
UPDATE APPLICATION.DATASINK_CONFIG SET PROCESS_GROUP_DEPENDENCIES='datasink.mongodb' WHERE FQN='com.deleidos.rtws.ext.datasink.MongoDbDataSink'; 

-- Disable selection of alertviz by user for 1.0
DELETE FROM COMMONDB.APPLICATION.WEBAPPS_CONFIG WHERE FQN='com.deleidos.rtws.alertviz';