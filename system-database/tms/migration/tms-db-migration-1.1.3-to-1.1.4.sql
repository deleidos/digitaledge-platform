
INSERT INTO APPLICATION.DATASINK_CONFIG (FQN, CAN_AUTOSCALE, USES_BLOCK_STORAGE, SCALE_UP_FACTOR, INGEST_CONFIG_FILENAME, PIPELINE_XML_FILENAME, PIPELINE_XML_TEMPLATE, PROCESS_GROUP_DEPENDENCIES)
VALUES ('com.deleidos.rtws.ext.datasink.LuceneSummarizationDataSink', 'Y', 'Y', 2.0, '', '', '', 'datasink.lucene-sum');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, VPC_SUBNET, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('datasink.lucene-sum', 'datasink.default', 'public', 'lucene-sum-shard?.@build.domain@', 'ingest.rtws.saic.com,jmxhost.external.rtws.saic.com', null, 'm1.large','instance', 'includes searchapi', 'webapps-lucene-shard.ini', 'services.lucene-sum.xml', '{"default-num-volumes" : 1, "default-volume-size" : 50,"config-volume-size" : true, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true,"config-scaling" : true,"config-jms-persistence" : false }');

INSERT INTO APPLICATION.DATASINK_CONFIG (FQN, CAN_AUTOSCALE, USES_BLOCK_STORAGE, SCALE_UP_FACTOR, INGEST_CONFIG_FILENAME, PIPELINE_XML_FILENAME, PIPELINE_XML_TEMPLATE, PROCESS_GROUP_DEPENDENCIES)
VALUES ('com.deleidos.rtws.ext.datasink.HiveSummarizationDataSink', 'Y', 'Y', 2.0, '', '', '', 'zookeeper, hadoop.hbase.namenode-master, hadoop.jobtracker, hadoop.hbase.datanode.regionserver, hive.metastore, datasink.hive-sum');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, VPC_SUBNET, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('datasink.hive-sum', 'datasink.default', 'public', 'ingest-hive-sum?.@build.domain@', 'ingest.rtws.saic.com', null, 'm1.large','instance', null, 'cloudera-hive-v2.ini', 'services.hive-sum.xml', '{"default-num-volumes" : 0, "default-volume-size" : 0,"config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true,"config-scaling" : true,"config-jms-persistence" : false }');

UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{''default-num-volumes'' : 0, ''default-volume-size'' : 0,  ''config-volume-size'' : false, ''config-persistent-ip'' : false, ''config-instance-size'' : true, ''config-min-max'' : true,   ''config-scaling'' : false,  ''config-jms-persistence'' : false }' WHERE PROCESS_GROUP_NAME='datasink.exthdfs';

CREATE TABLE IF NOT EXISTS APPLICATION.IAAS_ACCOUNT_LIMITS (
    ACCOUNT_ID						NUMBER	NOT NULL UNIQUE,
    USED_INSTANCES					NUMBER	NOT NULL DEFAULT 0,
    MAX_INSTANCES					NUMBER	NOT NULL DEFAULT 20,
    FOREIGN KEY (ACCOUNT_ID) REFERENCES APPLICATION.IAAS_ACCOUNTS(ACCOUNT_ID)
    
);

DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='hadoop.hbase.datanode.regionserver.v2' OR PROCESS_GROUP_NAME='hadoop.hbase.namenode-master.v2' OR PROCESS_GROUP_NAME='zookeeper.v2' OR  PROCESS_GROUP_NAME='hadoop.jobtracker.v2';

UPDATE APPLICATION.DATASINK_CONFIG SET PROCESS_GROUP_DEPENDENCIES='zookeeper, hadoop.hbase.namenode-master, hadoop.jobtracker, hadoop.hbase.datanode.regionserver, hive.metastore, datasink.hbase' WHERE FQN='com.deleidos.rtws.ext.datasink.HbaseDataSink';

UPDATE APPLICATION.WEBAPPS_CONFIG SET DATASINK_DEPENDENCIES='' WHERE FQN='processgroup:hue.server';

UPDATE APPLICATION.PROCESS_GROUP_CONFIG SET CONFIG_PERMISSIONS='{"default-num-volumes" : 2, "default-volume-size" : 50, "config-volume-size" : true, "config-persistent-ip" : false, "config-instance-size" : true, "config-min-max" : true, "config-scaling" : true, "config-jms-persistence" : false }' WHERE PROCESS_GROUP_NAME='hadoop.hbase.datanode.regionserver' OR PROCESS_GROUP_NAME='hadoop.accumulo.datanode.tabletserver';

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG VALUES ('datasink.cassandra', 'datasink.default', 'private', 'cassandra-writer-node?.@build.domain@', 'ingest.rtws.saic.com', null, 'm1.small', 'instance', null, 'datasink-cassandra.ini', 'services.cassandra.xml', '{"default-num-volumes" : 0, "default-volume-size" : 0, "config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : true, "config-min-max" : true, "config-scaling" : false, "config-jms-persistence" : false }');
INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG VALUES ('cassandra.seednode', 'datasink.default', 'private', 'cassandra-seednode?.@build.domain@', 'ingest.rtws.saic.com', null, 'm1.large', 'instance', null, 'cassandra-node.ini', null, '{"default-num-volumes" : 2, "default-volume-size" : 50, "config-volume-size" : true, "config-persistent-ip" : false, "config-instance-size" : true, "config-min-max" : true, "config-scaling" : false, "config-jms-persistence" : false }');
INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG VALUES ('cassandra.datanode', 'datasink.default', 'private', 'cassandra-datanode?.@build.domain@', 'ingest.rtws.saic.com', null, 'm1.large', 'instance', null, 'cassandra-node.ini', null, '{"default-num-volumes" : 2, "default-volume-size" : 50, "config-volume-size" : true, "config-persistent-ip" : false, "config-instance-size" : true, "config-min-max" : true, "config-scaling" : false, "config-jms-persistence" : false }');
INSERT INTO APPLICATION.DATASINK_CONFIG VALUES ('com.deleidos.cassandra.datasink.CassandraDataSink', 'Y', 'N', 1, '', '', '', 'datasink.cassandra,cassandra.seednode,cassandra.datanode');

-- Commit
commit;