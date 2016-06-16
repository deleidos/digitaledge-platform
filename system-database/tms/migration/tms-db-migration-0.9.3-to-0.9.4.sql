INSERT INTO MACHINE_IMAGES VALUES('AWS','us-east-1','0.9.4','ami-88c96ee1','ami-66c96e0f','ami-88c96ee1','ami-66c96e0f');

ALTER TABLE APPLICATION.IAAS_ACCOUNTS ADD IAAS_SERVICE_NUMBER VARCHAR2(255);

UPDATE APPLICATION.DATASINK_CONFIG 
SET PIPELINE_XML_TEMPLATE = 
'<?xml version="1.0" encoding="UTF-8" ?>
<pipeline-definition xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<sink
		xsi:type="java:com.deleidos.rtws.ext.datasink.SubscriptionFilter"
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
</pipeline-definition>'
WHERE FQN='com.deleidos.rtws.ext.datasink.SubscriptionFilter';

UPDATE APPLICATION.DATASINK_CONFIG 
SET PIPELINE_XML_TEMPLATE = 
'<?xml version="1.0" encoding="UTF-8" ?> 
<pipeline-definition xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<sink
		xsi:type="java:com.deleidos.rtws.ext.datasink.HbaseDataSink"
		write-level="com.deleidos.rtws.ext.datasink.JsonMetadataPutProducer,com.deleidos.rtws.ext.datasink.JsonObjectPutProducer"
		processor-enabled="true"
		max-time-between-flush="60000"
		max-bytes-between-flush="80000">
		<name>Hbase</name>
	</sink>
</pipeline-definition>'
WHERE FQN='com.deleidos.rtws.ext.datasink.HbaseDataSink';

INSERT INTO APPLICATION.DATASINK_CONFIG VALUES('com.deleidos.rtws.ext.datasink.SleepProcessor', 'Y', 'N', 1.0, 'services.sleep.xml', 'pipeline.sleep.default.xml', 
'<?xml version="1.0" encoding="UTF-8" ?> 
<pipeline-definition xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<sink
		xsi:type="java:com.deleidos.rtws.ext.datasink.SleepProcessor"
		processor-enabled="true"
		max-time-between-flush="60000" 
		max-bytes-between-flush="80000" >
		<name>SleepProcessor</name>
	</sink>
</pipeline-definition>', 'ingest.sleep');

INSERT INTO APPLICATION.INSTANCE_TYPES VALUES('m1.medium', 64, 2, 3840);

UPDATE APPLICATION.PROCESS_GROUP_CONFIG
SET CONFIG_PERMISSIONS = '{"default-num-volumes" : 0, "default-volume-size" : 0,  "config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true, "config-scaling" : false, "config-jms-persistence" : false }'
WHERE PROCESS_GROUP_NAME='zookeeper';

UPDATE APPLICATION.PROCESS_GROUP_CONFIG
SET DEFAULT_INSTANCE_TYPE = 'm1.large', CONFIG_PERMISSIONS = '{"default-num-volumes" : 2, "default-volume-size" : 25,  "config-volume-size" : true, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true, "config-scaling" : true, "config-jms-persistence" : false }'
WHERE PROCESS_GROUP_NAME='datanode.regionserver';

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG VALUES ('ingest.sleep', 'datasink.default', 'ingest-sleep-node?.@build.domain@', 'ingest.rtws.saic.com', null, 'm1.large',	'instance', null, 'ingest.ini', 'services.sleep.xml', '{"default-num-volumes" : 0, "default-volume-size" : 0,  "config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : true,  "config-scaling" : false,  "config-jms-persistence" : false }');

UPDATE APPLICATION.TRANSPORT_CONFIG
SET TRANSPORT_XML_TEMPLATE =
'<transport-services xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:type="java:com.deleidos.rtws.transport.Services.UDPTransportService">
	<url>@messaging.external.connection.url@</url> 
	<user>@messaging.external.connection.user@</user>
	<password>@messaging.external.connection.password@</password>
	<queue>@messaging.queue.parse.name@</queue>
	<activemq-ssl-transport-factory xsi:type="java:com.deleidos.rtws.commons.net.jms.ActiveMQSslTransportFactory" />
	<listen-port></listen-port>
	<input-format></input-format>
	<max-record-size>1000000</max-record-size>
</transport-services>'
WHERE FQN = 'com.deleidos.rtws.transport.Services.UDPTransportService';

UPDATE APPLICATION.TRANSPORT_CONFIG
SET TRANSPORT_XML_TEMPLATE =
'<transport-services xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:type="java:com.deleidos.rtws.transport.Services.TCPTransportService">
	<url>@messaging.external.connection.url@</url>
	<user>@messaging.external.connection.user@</user>
	<password>@messaging.external.connection.password@</password>
	<queue>@messaging.queue.parse.name@</queue>
	<activemq-ssl-transport-factory xsi:type="java:com.deleidos.rtws.commons.net.jms.ActiveMQSslTransportFactory" />
	<listen-port></listen-port>
	<input-format></input-format>
	<splitter xsi:type="java:com.deleidos.rtws.core.framework.splitter.LineSplitter">
		<num-headers>0</num-headers>
	</splitter>
	<max-record-size>1000000</max-record-size>
</transport-services>'
WHERE FQN = 'com.deleidos.rtws.transport.Services.TCPTransportService';

UPDATE APPLICATION.TRANSPORT_CONFIG
SET TRANSPORT_XML_TEMPLATE =
'<transport-services xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:type="java:com.deleidos.rtws.transport.Services.JMSBridgeTransportService">
	<url>@messaging.external.connection.url@</url>
	<user>@messaging.external.connection.user@</user>
	<password>@messaging.external.connection.password@</password>
	<queue>@messaging.queue.parse.name@</queue>
	<activemq-ssl-transport-factory xsi:type="java:com.deleidos.rtws.commons.net.jms.ActiveMQSslTransportFactory" />
	<incoming-address></incoming-address> 
	<incoming-user></incoming-user>
	<incoming-password></incoming-password>
	<incoming-queue></incoming-queue>
	<input-format></input-format>
	<sleep-time>1000</sleep-time>
</transport-services>'
WHERE FQN = 'java:com.deleidos.rtws.transport.Services.JMSBridgeTransportService';

UPDATE APPLICATION.TRANSPORT_CONFIG
SET TRANSPORT_XML_TEMPLATE =
'<transport-services xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
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
	<bucket-name>rtws.test-dirscan</bucket-name>
	<file-name>testTransport.txt</file-name>
	<input-format>aws</input-format>
	<splitter xsi:type="java:com.deleidos.rtws.core.framework.splitter.LineSplitter">
		<num-headers>0</num-headers>
	</splitter>
	<max-record-size>1000000</max-record-size>
</transport-services>'
WHERE FQN = 'java:com.deleidos.rtws.transport.Services.S3FileTransportService';

UPDATE APPLICATION.TRANSPORT_CONFIG
SET DESCRIPTION = 'Transports files dumped into an S3 Bucket (files are deleted afterwards)',
TRANSPORT_XML_TEMPLATE =
'<transport-services xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:type="java:com.deleidos.rtws.transport.Services.S3FileTransportService">
	<url>@messaging.external.connection.url@</url>
	<user>@messaging.external.connection.user@</user>
	<password>@messaging.external.connection.password@</password>
	<queue>@messaging.queue.parse.name@</queue>
	<activemq-ssl-transport-factory xsi:type="java:com.deleidos.rtws.commons.net.jms.ActiveMQSslTransportFactory">
		<key-store>../conf/ssl-keystore</key-store>
		<key-store-password>rtws100</key-store-password>
		<trust-store>../conf/ssl-keystore</trust-store>
		<trust-store-password>rtws100</trust-store-password>
	</activemq-ssl-transport-factory>
	<storage-interface xsi:type="java:com.deleidos.rtws.commons.cloud.platform.jetset.JetSetStorageService">
			<connection-factory xsi:type="com.deleidos.rtws.commons.cloud.platform.jetset.JetSetConnectionFactory">
				<properties-file>aws.properties</properties-file>
				<storage-endpoint>s3.amazonaws.com</storage-endpoint>
				<storage-port-number>80</storage-port-number>
				<storage-virtual-path></storage-virtual-path>
			</connection-factory>
	</storage-interface>
	<bucket-name>rtws.test-dirscan-polling</bucket-name>
	<file-name></file-name>
	<input-format>aws</input-format>
	<splitter xsi:type="java:com.deleidos.rtws.core.framework.splitter.LineSplitter">
		<num-headers>0</num-headers>
	</splitter>
	<max-record-size>1000000</max-record-size>
</transport-services>'
WHERE FQN = 'java:com.deleidos.rtws.transport.Services.PollingS3FileTransportService';

UPDATE APPLICATION.TRANSPORT_CONFIG
SET TRANSPORT_XML_TEMPLATE =
'<transport-services xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:type="java:com.deleidos.rtws.transport.Services.URLTransportService">
	<url>@messaging.external.connection.url@</url>
	<user>@messaging.external.connection.user@</user>
	<password>@messaging.external.connection.password@</password>
	<queue>@messaging.queue.parse.name@</queue>
	<activemq-ssl-transport-factory xsi:type="java:com.deleidos.rtws.commons.net.jms.ActiveMQSslTransportFactory" />
	<read-address></read-address>
	<input-format></input-format>
	<splitter xsi:type="java:com.deleidos.rtws.core.framework.splitter.LineSplitter">
		<num-headers>0</num-headers>
	</splitter>
	<max-record-size>1000000</max-record-size>
</transport-services>'
WHERE FQN = 'com.deleidos.rtws.transport.Services.URLTransportService';

UPDATE APPLICATION.CURRENT_DEFAULTS SET SW_VERSION_ID='0.9.4' WHERE IAAS_SERVICE_NAME='AWS';

ALTER TABLE APPLICATION.IAAS_ACCOUNTS ADD COLUMN AWS_ACCOUNT_EMAIL VARCHAR2(64);
