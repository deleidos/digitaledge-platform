INSERT INTO APPLICATION.SOFTWARE_RELEASES VALUES('0.9.9', 'The beta software release version', '');

INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-east-1', '0.9.9', 'ami-b38d22da', 'ami-118d2278', 'ami-b38d22da', 'ami-118d2278');
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-west-1', '0.9.9', 'ami-b38d22da', 'ami-118d2278', 'ami-b38d22da', 'ami-118d2278');
INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-west-2', '0.9.9', 'ami-b38d22da', 'ami-118d2278', 'ami-b38d22da', 'ami-118d2278');

UPDATE APPLICATION.TRANSPORT_CONFIG
SET TRANSPORT_XML_TEMPLATE='<transport-services xmlns:xsi=""http://www.w3.org/2001/XMLSchema-instance""
	xsi:type=""java:com.deleidos.rtws.transport.Services.S3FileTransportService"">
	<url>@messaging.external.connection.url@</url>
	<user>@messaging.external.connection.user@</user>
	<password>@messaging.external.connection.password@</password>
	<queue>@messaging.queue.parse.name@</queue>
	<activemq-ssl-transport-factory xsi:type=""java:com.deleidos.rtws.commons.net.jms.ActiveMQSslTransportFactory"" />
	<storage-interface xsi:type=""java:com.deleidos.rtws.commons.cloud.platform.jetset.JetSetStorageService"">
			<connection-factory xsi:type=""com.deleidos.rtws.commons.cloud.platform.jetset.JetSetConnectionFactory"">
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
	<splitter xsi:type=""java:com.deleidos.rtws.core.framework.splitter.LineSplitter"">
		<num-headers>0</num-headers>
	</splitter>
	<max-record-size>1000000</max-record-size>
</transport-services>'
WHERE FQN='com.deleidos.rtws.transport.Services.S3FileTransportService';

DELETE FROM APPLICATION.TRANSPORT_CONFIG WHERE FQN = 'com.deleidos.rtws.transport.Services.PollingS3FileTransportService';

UPDATE APPLICATION.AVAILABILITY_ZONES 
SET PROPERTIES_FILE='/usr/local/rtws/conf/aws.properties'
WHERE PROPERTIES_FILE='aws.properties';
