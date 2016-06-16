ALTER TABLE APPLICATION.IAAS_ACCOUNTS ADD COLUMN IF NOT EXISTS DOMAIN_NAME_SUFFIX VARCHAR(255) NOT NULL DEFAULT 'rtsaic.com';

ALTER TABLE APPLICATION.IAAS_ACCOUNTS ADD COLUMN IF NOT EXISTS GATEWAY_PUBLIC_DNS VARCHAR(255);

ALTER TABLE APPLICATION.AVAILABILITY_ZONES ADD COLUMN IF NOT EXISTS DESCRIPTION VARCHAR(512);

ALTER TABLE APPLICATION.AVAILABILITY_ZONES ADD COLUMN IF NOT EXISTS PROPERTIES_FILE VARCHAR(64) NOT NULL DEFAULT 'aws.properties';

ALTER TABLE APPLICATION.AVAILABILITY_ZONES ADD COLUMN IF NOT EXISTS STORAGE_ENDPOINT VARCHAR(256) NOT NULL DEFAULT 'http://s3.amazonaws.com:80';

ALTER TABLE APPLICATION.AVAILABILITY_ZONES ADD COLUMN IF NOT EXISTS SERVICE_ENDPOINT VARCHAR(256) NOT NULL DEFAULT 'http://ec2.us-east-1.amazonaws.com:80';

UPDATE APPLICATION.AVAILABILITY_ZONES 
SET DESCRIPTION='Amazon Web Services', SERVICE_ENDPOINT='http://ec2.us-east-1.amazonaws.com:80'
WHERE IAAS_REGION='us-east-1';

UPDATE APPLICATION.AVAILABILITY_ZONES 
SET DESCRIPTION='Amazon Web Services', SERVICE_ENDPOINT='http://ec2.us-west-1.amazonaws.com:80'
WHERE IAAS_REGION='us-west-1';

UPDATE APPLICATION.AVAILABILITY_ZONES 
SET DESCRIPTION='Amazon Web Services', SERVICE_ENDPOINT='http://ec2.us-west-2.amazonaws.com:80'
WHERE IAAS_REGION='us-west-2';

INSERT INTO APPLICATION.DATASINK_CONFIG (FQN, CAN_AUTOSCALE, USES_BLOCK_STORAGE, SCALE_UP_FACTOR, INGEST_CONFIG_FILENAME, PIPELINE_XML_FILENAME, PIPELINE_XML_TEMPLATE, PROCESS_GROUP_DEPENDENCIES)
VALUES 
('com.deleidos.rtws.ext.datasink.HiveDataSink', 'Y', 'Y', 2.0, 'services.hive.xml', 'pipeline.hive.default.xml', 
'<pipeline-definition xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<sink
		xsi:type="java:com.deleidos.rtws.ext.datasink.HiveDataSink"
		processor-enabled="true"	
		max-bytes-between-flush="80000" 
		hdfs-working-folder="/tmp/hive-datasink"
		hive-jdbc-hostname="localhost"
		hive-jdbc-port="10000"
		namenode-hostname="namenode.domain"
		namenode-port="8020"
		jobtracker-hostname="jobtracker.domain"
		jobtracker-port="8010"
		replication-factor="2"
		use-complex-schema="false">
		<name>Hive Data Sink</name>
	</sink>
</pipeline-definition>', 'zookeeper, hadoop.hbase.namenode-master, hadoop.jobtracker, hadoop.hbase.datanode.regionserver, hive.metastore, datasink.hive');

INSERT INTO APPLICATION.DATASINK_CONFIG (FQN, CAN_AUTOSCALE, USES_BLOCK_STORAGE, SCALE_UP_FACTOR, INGEST_CONFIG_FILENAME, PIPELINE_XML_FILENAME, PIPELINE_XML_TEMPLATE, PROCESS_GROUP_DEPENDENCIES)
VALUES 
('com.deleidos.rtws.ext.datasink.ExternalHiveDataSink', 'Y', 'Y', 2.0, 'services.hive.xml', 'pipeline.hive.default.xml', '<?xml version="1.0" encoding="UTF-8" ?> 
<pipeline-definition xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<sink
		xsi:type="java:com.deleidos.rtws.ext.datasink.ExternalHiveDataSinkTest"
		processor-enabled="true"	
		max-bytes-between-flush="80000"
		hdfs-working-folder="/tmp/hive-datasink"
		hive-jdbc-hostname="192.168.6.61"
		hive-jdbc-port="10000"
		namenode-hostname="namenode.domain"
		namenode-port=""8020""
		jobtracker-hostname="jobtracker.domain"
		jobtracker-port="8010"
		replication-factor="2"
		use-complex-schema="false">
		<name>Hive Data Sink</name>
	</sink>
</pipeline-definition>', 'zookeeper, datasink.hive.external');

INSERT INTO APPLICATION.DATASINK_CONFIG (FQN, CAN_AUTOSCALE, USES_BLOCK_STORAGE, SCALE_UP_FACTOR, INGEST_CONFIG_FILENAME, PIPELINE_XML_FILENAME, PIPELINE_XML_TEMPLATE, PROCESS_GROUP_DEPENDENCIES)
VALUES 
('com.deleidos.rtws.ext.datasink.JMSDataSink', 'Y', 'Y', 2.0, 'services.jms-datasink.xml', 'pipeline.jms-datasink.default.xml', '<?xml version="1.0" encoding="UTF-8" ?> 
<pipeline-definition xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<sink
		xsi:type="java:com.deleidos.rtws.ext.datasink.JMSDataSink"
		processor-enabled="true"
		max-time-between-flush="60000"
		max-bytes-between-flush="80000"
		persistent="false">
		<name>JMS Data Sink</name>
	</sink>
</pipeline-definition>', 'datasink.jms');

INSERT INTO APPLICATION.DATASINK_CONFIG (FQN, CAN_AUTOSCALE, USES_BLOCK_STORAGE, SCALE_UP_FACTOR, INGEST_CONFIG_FILENAME, PIPELINE_XML_FILENAME, PIPELINE_XML_TEMPLATE, PROCESS_GROUP_DEPENDENCIES)
VALUES 
('com.deleidos.rtws.ext.datasink.PentahoBiPlatformDataSink', 'N', 'Y', 0, 'services.pentaho-bi.xml', 'pipeline.pentaho-bi.default.xml', '<?xml version="1.0"" encoding="UTF-8" ?> 
<pipeline-definition xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<sink
		xsi:type="java:com.deleidos.rtws.ext.datasink.PentahoBiPlatformDataSink"
		processor-enabled="false"
		persistent="false">
		<name>Pentaho BI Platform</name>
	</sink>
</pipeline-definition>', 'pentaho.bi.server');

INSERT INTO APPLICATION.DATASINK_CONFIG (FQN, CAN_AUTOSCALE, USES_BLOCK_STORAGE, SCALE_UP_FACTOR, INGEST_CONFIG_FILENAME, PIPELINE_XML_FILENAME, PIPELINE_XML_TEMPLATE, PROCESS_GROUP_DEPENDENCIES)
VALUES 
('com.deleidos.rtws.ext.datasink.DynamoDbDataSink', 'N', 'Y', 0.0, 'services.dynamo.xml', 'pipeline.dynamo.default.xml', '<?xml version="1.0" encoding="UTF-8" ?> 
<pipeline-definition xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<sink
		xsi:type="java:com.deleidos.rtws.ext.datasink.DynamoDbDataSink"
		read-throughput="10"
		processor-enabled="true">
		<name>Dynamo DB Data Sink</name>
	</sink>
</pipeline-definition>', 'datasink.dynamodb');

INSERT INTO APPLICATION.DATASINK_CONFIG (FQN, CAN_AUTOSCALE, USES_BLOCK_STORAGE, SCALE_UP_FACTOR, INGEST_CONFIG_FILENAME, PIPELINE_XML_FILENAME, PIPELINE_XML_TEMPLATE, PROCESS_GROUP_DEPENDENCIES)
VALUES 
('com.deleidos.rtws.ext.datasink.ExternalHdfsDataSink', 'N', 'Y', 0.0, 'services.exthdfs.xml', 'pipeline.exthdfs.default.xml', '<?xml version="1.0" encoding="UTF-8" ?> 
<pipeline-definition xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<sink
		xsi:type="java:com.deleidos.rtws.ext.datasink.ExternalHdfsDataSink"
		namenode-hostname="namenode.domain"
		namenode-port="8020"
		target-folder="/tmp/external_hdfs_data_sink"
		processor-enabled="true">
		<name>External HDFS Data Sink</name>
	</sink>
</pipeline-definition>', 'datasink.exthdfs');

INSERT INTO APPLICATION.DATASINK_CONFIG (FQN, CAN_AUTOSCALE, USES_BLOCK_STORAGE, SCALE_UP_FACTOR, INGEST_CONFIG_FILENAME, PIPELINE_XML_FILENAME, PIPELINE_XML_TEMPLATE, PROCESS_GROUP_DEPENDENCIES)
VALUES 
('com.deleidos.rtws.ext.datasink.LuceneIndexingDataSink', 'Y', 'Y', 2.0, 'services.zoie.xml', 'pipeline.zoie.default.xml', '<?xml version="1.0" encoding="UTF-8" ?> 
<pipeline-definition xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<sink
		xsi:type="java:com.deleidos.rtws.ext.datasink.LuceneIndexingDataSink"
		max-records-between-flush="1000"
		processor-enabled="true"
		zoie-batch-size="10000"
		zoie-batch-delay="60000">
		<name>Zoie Indexing</name>
		<index-path>@lucene.path.index@</index-path>
		<system-repository xsi:type="java:com.deleidos.rtws.commons.util.repository.ConfigDirSystemRepository">
			<config-path>/mnt/appfs</config-path>
		</system-repository>
	</sink>
</pipeline-definition>', 'datasink.lucene');

INSERT INTO APPLICATION.DATASINK_CONFIG (FQN, CAN_AUTOSCALE, USES_BLOCK_STORAGE, SCALE_UP_FACTOR, INGEST_CONFIG_FILENAME, PIPELINE_XML_FILENAME, PIPELINE_XML_TEMPLATE, PROCESS_GROUP_DEPENDENCIES)
VALUES 
('com.deleidos.rtws.ext.datasink.SaicAlertingDataSink', 'Y', 'N', 0.75, 'services.alert.xml', 'pipeline.alert.default.xml', '<?xml version="1.0" encoding="UTF-8" ?> 
<pipeline-definition xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<sink
		xsi:type="java:com.deleidos.rtws.ext.datasink.SaicAlertingDataSink"
		processor-enabled="true">
		<name>Alert Subscription Filter</name>
		<subscription-manager>
			<cache-loader cache-refresh-interval="60000">
				<dao xsi:type="java:com.deleidos.rtws.commons.dao.FilterDao">
		            <data-source xsi:type="java:com.deleidos.rtws.commons.dao.source.H2ConnectionPool">
		                    <URL>@h2.app.connection.url@</URL>
		                    <user>@h2.app.connection.user@</user>
		                    <password>@h2.app.connection.password@</password>
		            </data-source>
				</dao>
			</cache-loader>
		</subscription-manager>
		<aws-connection-factory xsi:type="java:com.deleidos.rtws.commons.cloud.platform.aws.AwsConnectionFactory">
			<properties-file>aws.properties</properties-file>
		</aws-connection-factory>
		<connection-factory xsi:type="java:org.apache.activemq.ActiveMQConnectionFactory">
			<broker-uRL>@messaging.alerts.connection.url@</broker-uRL>
			<user-name>@messaging.external.connection.user@</user-name>
			<password>@messaging.external.connection.password@</password>
			<always-sync-send>true</always-sync-send>
		</connection-factory>
		<topic>@messaging.topic.alert.name@</topic>
		<send-email>false</send-email>
		<filtering-email-sender xsi:type="java:com.deleidos.rtws.ext.net.email.FilteringEmailSender">
			<email-server>Amazon</email-server>
			<email-port>25</email-port>
			<email-from>dev@rtsaic.com</email-from>
			<email-from-password></email-from-password>
			<dao xsi:type="java:com.deleidos.rtws.commons.dao.EmailDao">
				<data-source xsi:type="java:com.deleidos.rtws.commons.dao.source.H2ConnectionPool">
					<URL>@h2.app.connection.url@</URL>
					<user>@h2.app.connection.user@</user>
					<password>@h2.app.connection.password@</password>
				</data-source>
			</dao>
		</filtering-email-sender>
	</sink>
</pipeline-definition>', 'datasink.alert');

INSERT INTO APPLICATION.DATASINK_CONFIG (FQN, CAN_AUTOSCALE, USES_BLOCK_STORAGE, SCALE_UP_FACTOR, INGEST_CONFIG_FILENAME, PIPELINE_XML_FILENAME, PIPELINE_XML_TEMPLATE, PROCESS_GROUP_DEPENDENCIES)
VALUES 
('com.deleidos.rtws.ext.datasink.ScriptingDataSink', 'Y', 'N', 1.0, 'services.script.xml', 'pipeline.script.default.xml', '<?xml version="1.0" encoding="UTF-8" ?> 
<pipeline-definition xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<sink xsi:type="java:com.deleidos.rtws.ext.datasink.ScriptingDataSink" 
		max-time-between-flush="60000"
		max-bytes-between-flush="80000" 
		processor-enabled="true">
		<name>ScriptJSR_223</name> 
		<engine-name>@script.enginename@</engine-name> 
		<script-file>@script.absolutefilepath@</script-file>
		<parameter>/tmp/groovy.out</parameter>
	</sink>
</pipeline-definition>', 'datasink.script');

INSERT INTO APPLICATION.DATASINK_CONFIG (FQN, CAN_AUTOSCALE, USES_BLOCK_STORAGE, SCALE_UP_FACTOR, INGEST_CONFIG_FILENAME, PIPELINE_XML_FILENAME, PIPELINE_XML_TEMPLATE, PROCESS_GROUP_DEPENDENCIES)
VALUES 
('com.deleidos.rtws.ext.datasink.SleepDataSink', 'Y', 'N', 1.0, 'services.sleep.xml', 'pipeline.sleep.default.xml', '<?xml version="1.0" encoding="UTF-8" ?> 
<pipeline-definition xmlns:xsi=""http://www.w3.org/2001/XMLSchema-instance">
	<sink
		xsi:type="java:com.deleidos.rtws.ext.datasink.SleepDataSink"
		processor-enabled="true"
		max-time-between-flush="60000" 
		max-bytes-between-flush="80000" >
		<name>Sleep Datasink</name>
	</sink>
</pipeline-definition>', 'datasink.sleep');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('hive.metastore', 'datasink.default', 'hive-metastore.@build.domain@', null, null, 'm1.large', 'instance', null, 'cloudera-hive-metastore.ini', null, '{"default-num-volumes" : 4, "default-volume-size" : 25, "config-volume-size" : true,  "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : false, "config-scaling"" : false, "config-jms-persistence"" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('gateway','webapp.default',	'auth.@build.domain@',null, 'auth-@build-domain@', 'm1.large','instance',null , 'anchor.ini', null, '{"default-num-volumes" : 4, "default-volume-size" : 25, "config-volume-size" : true,"config-persistent-ip" : true,"config-instance-size" : false, "config-min-max" : false, "config-scaling" : false, "config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('hadoop.hbase.namenode-master', 'internal.default', 'namenode.@build.domain@', null, null, 'm1.large', 'instance', null, 'cloudera-master.ini', null, '{"default-num-volumes" : 1, "default-volume-size" : 200,"config-volume-size" : true, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : false, "config-scaling" : false, "config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('hadoop.jobtracker', 'internal.default', 'jobtracker.@build.domain@', null, null, 'm1.large',	'instance', null,	'cloudera-jobtracker.ini', null, '{"default-num-volumes" : 0, "default-volume-size" : 0,"config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : false, "config-scaling" : false, "config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('hadoop.hbase.datanode.regionserver', 'internal.default',	'dtanrgs?.@build.domain@', null, null, 'm1.large', 'instance', null, 'cloudera-datanode-regionserver.ini', null,'{"default-num-volumes" : 2, "default-volume-size" : 25,"config-volume-size" : true, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true, "config-scaling" : true, "config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('datasink.alert', 'datasink.default', 'ingest-alert-node?.@build.domain@', 'ingest.rtws.saic.com', null, 'm1.large','instance', null, 'ingest.ini', 'services.alert.xml', '{"default-num-volumes" : 0, "default-volume-size" : 0,"config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true,"config-scaling" : true,"config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('datasink.hbase', 'datasink.default', 'ingest-hbase?.@build.domain@', 'ingest.rtws.saic.com', null, 'm1.large', 'instance', null, 'datasink-hbase.ini', 'services.hbase.xml','{"default-num-volumes" : 0, "default-volume-size" : 0,"config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true,"config-scaling" : true,"config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('datasink.dynamo', 'datasink.default', 'ingest-dynamo?.@build.domain@', 'ingest.rtws.saic.com', null, 'm1.large','instance', null, 'datasink-dynamo.ini', 'services.dynamo.xml','{"default-num-volumes" : 0, "default-volume-size" : 0,"config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true,"config-scaling" : true,"config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('datasink.exthdfs', 'datasink.default',	'ingest-exthdfs?.@build.domain@', 'ingest.rtws.saic.com', null, 'm1.large', 'instance', null, 'datasink-exthdfs.ini', 'services.exthdfs.xml',	'{"default-num-volumes" : 0, "default-volume-size" : 0,"config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : true, "config-min-max" : true,"config-scaling" : true,"config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('datasink.hive', 'datasink.default', 'ingest-hive?.@build.domain@', 'ingest.rtws.saic.com', null, 'm1.large', 'instance', null, 'cloudera-hive.ini', 'services.hive.xml','{"default-num-volumes" : 0, "default-volume-size" : 0,"config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true,"config-scaling" : true,"config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('datasink.hive.external', 'datasink.default', 'ingest-hive-external?.@build.domain@', 'ingest.rtws.saic.com', null, 'm1.large','instance', null, 'ingest.ini','services.hive.xml','{"default-num-volumes" : 0, "default-volume-size" : 0,"config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true,"config-scaling" : true,"config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('datasink.gotometrics', 'datasink.default', 'ingest-gotometrics-node?.@build.domain@', 'ingest.rtws.saic.com', null, 'm1.large', 'instance', null, 'ingest.ini', 'services.gotometrics.xml', '{"default-num-volumes" : 0, "default-volume-size" : 0,"config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true,"config-scaling" : true,"config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('datasink.script', 'datasink.deafult', 'ingest-script-node?.@build.domain@', 'ingest.rtws.saic.com', null, 'm1.large', 'instance', null, 'ingest.ini','services.script.xml','{"default-num-volumes" : 0, "default-volume-size" : 0,"config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true,"config-scaling" : true,"config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('datasink.sleep', 'datasink.default', 'ingest-sleep-node?.@build.domain@', 'ingest.rtws.saic.com', null, 'm1.large','instance', null, 'ingest.ini', 'services.sleep.xml', '{"default-num-volumes" : 0, "default-volume-size" : 0,"config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true,"config-scaling" : false,"config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('datasink.mongodb', 'datasink.default', 'mongodb-writer-node?.@build.domain@', 'ingest.rtws.saic.com', null, 'm1.large', 'instance', null, 'mongo.ini', 'services.mongo.xml','{"default-num-volumes" : 1, "default-volume-size" : 75,"config-volume-size" : true, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true,"config-scaling" : true,"config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('webapps.default', 'webapp.default', 'default.@build.domain@', null, '@build-domain@', 'm1.small', 'instance', 'user', 'webapps-default.ini', null, '{"default-num-volumes" : 0, "default-volume-size" : 0,"config-volume-size" : false, "config-persistent-ip" : true,"config-instance-size" : true,"config-min-max" : false, "config-scaling" : false, "config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('webapps.main', 'webapp.default',	'default.@build.domain@', null, '@build-domain@', 'm1.small', 'instance', 'user', 'webapps-default.ini', null, '{"default-num-volumes" : 0, "default-volume-size" : 0,"config-volume-size" : false, "config-persistent-ip" : true,"config-instance-size" : true,"config-min-max" : false, "config-scaling" : false, "config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('datasink.lucene', 'datasink.default', 'lucene-shard?.@build.domain@', 'ingest.rtws.saic.com,jmxhost.external.rtws.saic.com', null, 'm1.large', 'instance', 'includes search searchapi','webapps-lucene-shard.ini', null, '{"default-num-volumes" : 4, "default-volume-size" : 50, "config-volume-size" : true,"config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true,"config-scaling" : true,"config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('pentaho.bi.server', 'datasink.default', 'pentaho-bi.@build.domain@', null,	null,	'm1.large', 'instance', null, 'pentaho-bi.ini', null, '{"default-num-volumes" : 4, "default-volume-size" : 25,"config-volume-size" : true, "config-persistent-ip" : true, "config-instance-size" : true, "config-min-max" : false,"config-scaling" : false,"config-jms-persistence" : false }');

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG (PROCESS_GROUP_NAME, SECURITY_GROUP, INTERNAL_DOMAIN_NAME, MANAGEMENT_INTERFACES, PUBLIC_DOMAIN_NAME, DEFAULT_INSTANCE_TYPE, INSTANCE_STORAGE, FIXED_WEBAPPS, MANIFEST_FILENAME, INGEST_CONFIG_FILENAME, CONFIG_PERMISSIONS)
VALUES ('datasink.jms', 'datasink.default', 'datasink-jms?.@build.domain@', null, null, 'm1.large', 'instance', null, 'ingest.ini', 'services.jms.xml', '{"default-num-volumes" : 0, "default-volume-size" : 0,"config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : true, "config-min-max" : false,"config-scaling" : false,"config-jms-persistence" : false }');

UPDATE APPLICATION.TRANSPORT_CONFIG
SET TRANSPORT_XML_TEMPLATE='<transport-services xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:type="java:com.deleidos.rtws.transport.Services.S3FileTransportService">
	<url>@messaging.external.connection.url@</url>
	<user>@messaging.external.connection.user@</user>
	<password>@messaging.external.connection.password@</password>
	<queue>@messaging.queue.parse.name@</queue>
	<activemq-ssl-transport-factory xsi:type="java:com.deleidos.rtws.commons.net.jms.ActiveMQSslTransportFactory" />
	<storage-interface xsi:type="java:com.deleidos.rtws.commons.cloud.platform.jetset.JetSetStorageService">
			<connection-factory xsi:type="com.deleidos.rtws.commons.cloud.platform.jetset.JetSetConnectionFactory">
				<properties-file>aws.properties</properties-file>
				<storage-endpoint>s3.amazonaws.com</storage-endpoint>
				<storage-port-number>80</storage-port-number>
				<storage-virtual-path></storage-virtual-path>
			</connection-factory>
	</storage-interface>
	<bucket-name></bucket-name>
	<file-name></file-name>
	<header></header>
	<input-format></input-format>
	<splitter xsi:type="java:com.deleidos.rtws.core.framework.splitter.LineSplitter">
		<num-headers>0</num-headers>
	</splitter>
	<max-record-size>1000000</max-record-size>
</transport-services>'
WHERE FQN='com.deleidos.rtws.transport.Services.S3FileTransportService';

UPDATE APPLICATION.TRANSPORT_CONFIG
SET TRANSPORT_XML_TEMPLATE='<transport-services xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:type="java:com.deleidos.rtws.transport.Services.PollingS3FileTransportService">
	<url>@messaging.external.connection.url@</url>
	<user>@messaging.external.connection.user@</user>
	<password>@messaging.external.connection.password@</password>
	<queue>@messaging.queue.parse.name@</queue>
	<activemq-ssl-transport-factory xsi:type="java:com.deleidos.rtws.commons.net.jms.ActiveMQSslTransportFactory" />
	<storage-interface xsi:type="java:com.deleidos.rtws.commons.cloud.platform.jetset.JetSetStorageService">
			<connection-factory xsi:type="com.deleidos.rtws.commons.cloud.platform.jetset.JetSetConnectionFactory">
				<properties-file>aws.properties</properties-file>
				<storage-endpoint>s3.amazonaws.com</storage-endpoint>
				<storage-port-number>80</storage-port-number>
				<storage-virtual-path></storage-virtual-path>
			</connection-factory>
	</storage-interface>
	<bucket-name></bucket-name>
	<polling-interval></polling-interval>
	<should-delete-source></should-delete-source>
	<header></header>
	<input-format></input-format>
	<splitter xsi:type="java:com.deleidos.rtws.core.framework.splitter.LineSplitter">
		<num-headers>0</num-headers>
	</splitter>
	<max-record-size>1000000</max-record-size>
</transport-services>'
WHERE FQN='com.deleidos.rtws.transport.Services.PollingS3FileTransportService';

INSERT INTO APPLICATION.TRANSPORT_CONFIG (FQN, DESCRIPTION, TRANSPORT_XML_TEMPLATE)
VALUES
('com.deleidos.rtws.transport.Services.DatabaseWatcherTransportService', 'A polling transport which will transmit the results of the SQL query into the system.', 
'<transport-services xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="java:com.deleidos.rtws.transport.Services.DatabaseWatcherTransportService">
		<url>@messaging.external.connection.url@</url>
		<user>@messaging.external.connection.user@</user>
		<password>@messaging.external.connection.password@</password>
		<queue>@messaging.queue.parse.name@</queue>
		<activemq-ssl-transport-factory xsi:type="java:com.deleidos.rtws.commons.net.jms.ActiveMQSslTransportFactory" />
		<input-format>file</input-format>
		<data-source xsi:type="java:com.deleidos.rtws.commons.dao.source.H2ConnectionPool">
			<URL>@h2.dim.connection.url@</URL>
			<user>@h2.dim.connection.user@</user>
			<password>@h2.dim.connection.password@</password>
		</data-source>
		<storage-interface
			xsi:type="java:com.deleidos.rtws.commons.cloud.platform.jetset.JetSetStorageService">
			<connection-factory
				xsi:type="com.deleidos.rtws.commons.cloud.platform.jetset.JetSetConnectionFactory">
				<properties-file>aws.properties</properties-file>
				<storage-endpoint>s3.amazonaws.com</storage-endpoint>
				<storage-port-number>80</storage-port-number>
				<storage-virtual-path></storage-virtual-path>
			</connection-factory>
		</storage-interface>
		<bucket-name></bucket-name>
		<file-key></file-key>
		<sleep-time></sleep-time>
		<memory-key-column></memory-key-column>
		<select-data-statement></select-data-statement>
		<max-record-size>1000000</max-record-size>
	</transport-services>');

INSERT INTO APPLICATION.TRANSPORT_CONFIG (FQN, DESCRIPTION, TRANSPORT_XML_TEMPLATE)
VALUES
('com.deleidos.rtws.transport.Services.DirectoryWatcherTransportService', 'A filesystem directory polling transport.', 
'<transport-services xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="java:com.deleidos.rtws.transport.Services.DirectoryWatcherTransportService">
		<url>@messaging.external.connection.url@</url>
		<user>@messaging.external.connection.user@</user>
		<password>@messaging.external.connection.password@</password>
		<queue>@messaging.queue.parse.name@</queue>
		<activemq-ssl-transport-factory xsi:type="java:com.deleidos.rtws.commons.net.jms.ActiveMQSslTransportFactory" />
		<check-interval></check-interval>
		<watched-directory></watched-directory>
		<input-format></input-format>
		<splitter xsi:type="java:com.deleidos.rtws.core.framework.splitter.LineSplitter">
			<num-headers>0</num-headers>
		</splitter>
		<max-record-size>1000000</max-record-size>
	</transport-services>');

UPDATE APPLICATION.WEBAPPS_CONFIG
SET DESCRIPTION='REST API for managing alert criteria used by the alerting data sink.'
WHERE FQN='com.deleidos.rtws.webapp.alertsapi';