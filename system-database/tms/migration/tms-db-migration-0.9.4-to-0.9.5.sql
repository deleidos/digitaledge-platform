ALTER TABLE APPLICATION.AVAILABILITY_ZONES ADD COLUMN IF NOT EXISTS IAAS_SW_BUCKET VARCHAR(32);

SET @TMS=SELECT SYSTEM_DOMAIN from APPLICATION.ACCOUNT_SYSTEMS where SYSTEM_DOMAIN like 'tms-%.rtsaic.com';
SET @BUCKET='prod';

SET @BUCKET=CASEWHEN(@TMS='tms-dev.rtsaic.com','rtws.appfs',@BUCKET);
SET @BUCKET=CASEWHEN(@TMS='tms-int.rtsaic.com','rtws.integration',@BUCKET);


SET @EASTBUCKET = CASEWHEN(@BUCKET='prod','rtws.prod.std',@BUCKET);
SET @WESTCABUCKET = CASEWHEN(@BUCKET='prod','rtws.prod.ca',@BUCKET);
SET @WESTORBUCKET = CASEWHEN(@BUCKET='prod','rtws.prod.org',@BUCKET);

update APPLICATION.AVAILABILITY_ZONES SET IAAS_SW_BUCKET=@EASTBUCKET where IAAS_REGION='us-east-1';
update APPLICATION.AVAILABILITY_ZONES SET IAAS_SW_BUCKET=@WESTCABUCKET where IAAS_REGION='us-west-1';
update APPLICATION.AVAILABILITY_ZONES SET IAAS_SW_BUCKET=@WESTORBUCKET where IAAS_REGION='us-west-2';

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
	<bucket-name></bucket-name>
	<file-name></file-name>
	<input-format></input-format>
	<splitter xsi:type="java:com.deleidos.rtws.core.framework.splitter.LineSplitter">
		<num-headers>0</num-headers>
	</splitter>
	<max-record-size>1000000</max-record-size>
</transport-services>'
WHERE FQN = 'java:com.deleidos.rtws.transport.Services.S3FileTransportService';

ALTER TABLE APPLICATION.ACCOUNT_SYSTEMS ALTER COLUMN SW_VERSION_ID VARCHAR2(64) NULL;

DELETE FROM APPLICATION.DATASINK_CONFIG WHERE FQN='com.deleidos.rtws.ext.datasink.ZoieIndexingProcessor'; 
DELETE FROM APPLICATION.DATASINK_CONFIG WHERE FQN='com.deleidos.rtws.ext.datasink.SubscriptionFilter';
DELETE FROM APPLICATION.DATASINK_CONFIG WHERE FQN='com.deleidos.rtws.ext.datasink.JSREngineSink';
DELETE FROM APPLICATION.DATASINK_CONFIG WHERE FQN='com.deleidos.rtws.ext.datasink.SleepProcessor';

DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='anchor';
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='datanode.regionserver';
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='ingest.alert';
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='ingest.hbase';
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='ingest.script';
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='ingest.sleep';
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='jobtracker';
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='mongodb.writer';
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='namenode.hbase-master';
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='webapps.lucene.shard';
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='webapps.default';
DELETE FROM APPLICATION.PROCESS_GROUP_CONFIG WHERE PROCESS_GROUP_NAME='data.nightly.node';

INSERT INTO APPLICATION.SYSTEM_SIZING VALUES('xsmall', 1, 'm1.small', 'Y',   1,        3,        1,          1);

INSERT INTO APPLICATION.SOFTWARE_RELEASES VALUE('0.9.5', 'The beta software release version', '');

INSERT INTO APPLICATION.MACHINE_IMAGES VALUES('AWS', 'us-east-1', '0.9.5', 'ami-cef85aa7', 'ami-38f85a51', 'ami-cef85aa7', 'ami-38f85a51');