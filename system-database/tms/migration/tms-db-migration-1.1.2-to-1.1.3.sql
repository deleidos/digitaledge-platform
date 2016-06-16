-- Disable Scaling for MongoDB Datasink
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 2, ''default-volume-size'' : 50,  ''config-volume-size'' : true, ''config-persistent-ip'' : false, ''config-instance-size'' : false, ''config-min-max'' : false,  ''config-scaling'' : false,  ''config-jms-persistence'' : false }' WHERE PROCESS_GROUP_NAME='datasink.mongodb';

-- Delete OBE process groups
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='hadoop.hbase.datanode.regionserver.v2';
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='hadoop.hbase.namenode-master.v2';
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='hadoop.jobtracker.v2'; 
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='zookeeper.v2';

-- D-02142 decision is to remove pentaho from being a selectable option in system builder
DELETE FROM APPLICATION.WEBAPPS_CONFIG WHERE FQN='processgroup:pentaho.bi.server';

ALTER USER INGEST ADMIN TRUE;
ALTER USER APPUSER ADMIN TRUE;

-- Commit
commit;