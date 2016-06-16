-- Enable Storage Metrics in System Monitor for Hadoop
UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET MANAGEMENT_INTERFACES='jmxhost.external.rtws.saic.com' WHERE PROCESS_GROUP_NAME='hadoop.accumulo.namenode-master' OR PROCESS_GROUP_NAME='hadoop.hbase.namenode-master';

-- Include Availability Zone in GATEWAY_ACCOUNTS table for Restart App
ALTER TABLE APPLICATION.GATEWAY_ACCOUNTS ADD COLUMN AZONE VARCHAR2(64) BEFORE REGISTRATION_TIMESTAMP;

-- Commit
commit;