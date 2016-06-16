-- Statements for Upgrade to the 1.1.3 release

-- Disable Scaling for MongoDB Datasink
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 2, ''default-volume-size'' : 50,  ''config-volume-size'' : true, ''config-persistent-ip'' : false, ''config-instance-size'' : false, ''config-min-max'' : false,  ''config-scaling'' : false,  ''config-jms-persistence'' : false }' WHERE PROCESS_GROUP_NAME='datasink.mongodb';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 0, ''default-volume-size'' : 0,  ''config-volume-size'' : false, ''config-persistent-ip'' : false, ''config-instance-size'' : true, ''config-min-max'' : true,   ''config-scaling'' : false,  ''config-jms-persistence'' : false }' WHERE PROCESS_GROUP_NAME='datasink.exthdfs';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 0, ''default-volume-size'' : 0,  ''config-volume-size'' : false, ''config-persistent-ip'' : false, ''config-instance-size'' : false, ''config-min-max'' : true,  ''config-scaling'' : false,  ''config-jms-persistence'' : false }' WHERE PROCESS_GROUP_NAME='datasink.jsontojdbc';


-- Delete OBE process groups
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='hadoop.hbase.datanode.regionserver.v2';
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='hadoop.hbase.namenode-master.v2';
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='hadoop.jobtracker.v2'; 
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='zookeeper.v2';


-- D-02142 decision is to remove pentaho from being a selectable option in system builder
DELETE FROM APPLICATION.WEBAPPS_CONFIG WHERE FQN='processgroup:pentaho.bi.server';

-- TK-06289 defaulting process groups to use just one volume (no raid) because of Euca raiding issues
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 1, ''default-volume-size'' : 30, ''config-volume-size'' : true,  ''config-persistent-ip'' : true,  ''config-instance-size'' : false, ''config-min-max'' : false, ''config-scaling'' : false, ''config-jms-persistence'' : false }' WHERE PROCESS_GROUP_NAME='gateway';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 1, ''default-volume-size'' : 30, ''config-volume-size'' : true,  ''config-persistent-ip'' : false, ''config-instance-size'' : false, ''config-min-max'' : false, ''config-scaling'' : false, ''config-jms-persistence'' : false }' WHERE PROCESS_GROUP_NAME='hive.metastore';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 1, ''default-volume-size'' : 100,  ''config-volume-size'' : true, ''config-persistent-ip'' : false, ''config-instance-size'' : false, ''config-min-max'' : false, ''config-scaling'' : false, ''config-jms-persistence'' : false }' WHERE PROCESS_GROUP_NAME='hadoop.hbase.namenode-master';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 1, ''default-volume-size'' : 100,  ''config-volume-size'' : true, ''config-persistent-ip'' : false, ''config-instance-size'' : false, ''config-min-max'' : true,  ''config-scaling'' : true, ''config-jms-persistence'' : false }' WHERE PROCESS_GROUP_NAME='hadoop.hbase.datanode.regionserver';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 1, ''default-volume-size'' : 100,  ''config-volume-size'' : true, ''config-persistent-ip'' : false, ''config-instance-size'' : false, ''config-min-max'' : false, ''config-scaling'' : false, ''config-jms-persistence'' : false }' WHERE PROCESS_GROUP_NAME='hadoop.hbase.namenode-master.v2';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 1, ''default-volume-size'' : 100,  ''config-volume-size'' : true, ''config-persistent-ip'' : false, ''config-instance-size'' : false, ''config-min-max'' : true,  ''config-scaling'' : true, ''config-jms-persistence'' : false }' WHERE PROCESS_GROUP_NAME='hadoop.hbase.datanode.regionserver.v2';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 1, ''default-volume-size'' : 30, ''config-volume-size'' : true,  ''config-persistent-ip'' : true,  ''config-instance-size'' : false, ''config-min-max'' : false, ''config-scaling'' : false, ''config-jms-persistence'' : false }' WHERE PROCESS_GROUP_NAME='jms.external';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 1, ''default-volume-size'' : 30, ''config-volume-size'' : true,  ''config-persistent-ip'' : true,  ''config-instance-size'' : false, ''config-min-max'' : false, ''config-scaling'' : false, ''config-jms-persistence'' : true  }' WHERE PROCESS_GROUP_NAME='jms.internal';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 1, ''default-volume-size'' : 100,  ''config-volume-size'' : true, ''config-persistent-ip'' : false, ''config-instance-size'' : false, ''config-min-max'' : false,  ''config-scaling'' : false,  ''config-jms-persistence'' : false }' WHERE PROCESS_GROUP_NAME='datasink.mongodb';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 1, ''default-volume-size'' : 100, ''config-volume-size'' : true,  ''config-persistent-ip'' : false, ''config-instance-size'' : false, ''config-min-max'' : true,  ''config-scaling'' : true,  ''config-jms-persistence'' : false }' WHERE PROCESS_GROUP_NAME='datasink.lucene';
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 1, ''default-volume-size'' : 30, ''config-volume-size'' : true,  ''config-persistent-ip'' : true,  ''config-instance-size'' : false, ''config-min-max'' : false, ''config-scaling'' : false, ''config-jms-persistence'' : false }' WHERE PROCESS_GROUP_NAME='pentaho.bi.server';


-- Commit
commit;