CREATE TABLE IF NOT EXISTS "APPLICATION"."IAAS_CONFIGURATION"
(
   IAAS_SERVICE_NAME varchar(64) PRIMARY KEY NOT NULL,
   DESCRIPTION varchar(512),
   SERVICE_INTERFACE_XML varchar(4000) NOT NULL,
   INTERNET_DNS_XML varchar(4000),
   STORAGE_INTERFACE_XML varchar(4000)
);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_13 ON "APPLICATION"."IAAS_CONFIGURATION"(IAAS_SERVICE_NAME);




INSERT INTO "APPLICATION"."IAAS_CONFIGURATION" (IAAS_SERVICE_NAME,DESCRIPTION,SERVICE_INTERFACE_XML,INTERNET_DNS_XML,STORAGE_INTERFACE_XML) VALUES ('AWS','Amazon Web Services','<serviceInterface xsi:type="awsServiceInterface">\n	<timeout>300000</timeout>\n	<connectionFactory>\n		<propertiesFile>aws.properties</propertiesFile>\n	</connectionFactory>\n</serviceInterface>','<internetDnsClient xsi:type="route53DnsClient">\n	<propFile>/usr/local/rtws/master/conf/aws.properties</propFile>\n	<queryUrl>https://route53.amazonaws.com/2011-05-05/change/</queryUrl>\n	<changeUrl>https://route53.amazonaws.com/2011-05-05/hostedzone/Z3K6TGL6UKGUC0/rrset</changeUrl>\n</internetDnsClient>','<storageInterface xsi:type="jetSetStorageService">\n		<connectionFactory>\n			<propertiesFile>aws.properties</propertiesFile>\n			<storageEndpoint>s3.amazonaws.com</storageEndpoint>\n			<storagePortNumber>80</storagePortNumber>\n			<storageVirtualPath></storageVirtualPath>\n		</connectionFactory>\n	</storageInterface>');
INSERT INTO "APPLICATION"."IAAS_CONFIGURATION" (IAAS_SERVICE_NAME,DESCRIPTION,SERVICE_INTERFACE_XML,INTERNET_DNS_XML,STORAGE_INTERFACE_XML) VALUES ('EUC','Eucalyptus','<serviceInterface xsi:type="awsServiceInterface">\n		<timeout>300000</timeout>\n		<connectionFactory>\n			<propertiesFile>aws.properties</propertiesFile>\n			<serviceEndpoint>http://172.16.0.129:8773/services/Eucalyptus</serviceEndpoint>\n		</connectionFactory>\n	</serviceInterface>',null,'<storageInterface xsi:type="jetSetStorageService">\n		<connectionFactory>\n			<propertiesFile>aws.properties</propertiesFile>\n			<storageEndpoint>172.16.0.129</storageEndpoint>\n			<storagePortNumber>8773</storagePortNumber>\n			<storageVirtualPath>/services/Walrus</storageVirtualPath>\n		</connectionFactory>\n	</storageInterface>');
