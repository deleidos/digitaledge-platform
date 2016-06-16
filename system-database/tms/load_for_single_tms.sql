-- Load Statements for setting up a single TMS in a box in AWS
-- the 

--  Have to populate based on current info   
--  INSERT INTO APPLICATION.IAAS_ACCOUNTS SELECT * FROM CSVREAD('/usr/local/rtws/system-database/euca-tms/iaas_accounts.csv');

INSERT INTO "APPLICATION"."IAAS_ACCOUNTS" (ACCOUNT_ID,ACCOUNT_NAME,DESCRIPTION,IAAS_SERVICE_NAME,IAAS_SERVICE_NUMBER,KEYPAIR_NAME,ANCHOR_INSTANCE_ID,AWS_ACCOUNT_EMAIL,DOMAIN_NAME_SUFFIX,EULA_ACCEPTED_TIMESTAMP,LICENSE_EXPIRATION_TIMESTAMP,VPC_ONLY) VALUES (1,'admin','TMS','AWS','112963766130','aws-tms-dev-us-east-1-20120423',null,'dev-tms@rtsaic.com','aws-dev.deleidos.com',CURRENT_TIMESTAMP(),null,'N');
-- INSERT INTO "APPLICATION"."IAAS_ACCOUNTS" (ACCOUNT_ID,ACCOUNT_NAME,DESCRIPTION,IAAS_SERVICE_NAME,IAAS_SERVICE_NUMBER,KEYPAIR_NAME,ANCHOR_INSTANCE_ID,AWS_ACCOUNT_EMAIL,DOMAIN_NAME_SUFFIX,EULA_ACCEPTED_TIMESTAMP,LICENSE_EXPIRATION_TIMESTAMP,VPC_ONLY) VALUES (1,'admin','TMS','AWS','IAAS_SERVICE_NUMBER','KEYPAIR_NAME',null,'AWS_ACCOUNT_EMAIL','DOMAIN_NAME_SUFFIX',null,null,'N');

INSERT INTO "APPLICATION"."GATEWAY_ACCOUNTS" (ACCOUNT_ID,INSTANCE_ID,PUBLIC_DNS,REGION,REGISTRATION_TIMESTAMP,AZONE) VALUES (1,null,'auth.tms-single.aws-dev.deleidos.com','us-east-1',null,'us-east-1c');

INSERT INTO APPLICATION.CURRENT_DEFAULTS SELECT * FROM CSVREAD('/usr/local/rtws/system-database/tms/current_defaults.csv');

INSERT INTO "APPLICATION"."IAAS_CONFIGURATION" (IAAS_SERVICE_NAME,DESCRIPTION,SERVICE_INTERFACE_XML,INTERNET_DNS_XML,STORAGE_INTERFACE_XML) VALUES ('AWS','Amazon Web Services','<serviceInterface xsi:type="awsServiceInterface">\n	<timeout>300000</timeout>\n	<connectionFactory>\n		<propertiesFile>aws.properties</propertiesFile>\n	</connectionFactory>\n</serviceInterface>','<internetDnsClient xsi:type="route53DnsClient">\n	<propFile>/usr/local/rtws/master/conf/aws.properties</propFile>\n	<queryUrl>https://route53.amazonaws.com/2011-05-05/change/</queryUrl>\n	<changeUrl>https://route53.amazonaws.com/2011-05-05/hostedzone/ZIHU7949LA4NJ/rrset</changeUrl>\n</internetDnsClient>','<storageInterface xsi:type="jetSetStorageService">\n		<connectionFactory>\n			<propertiesFile>aws.properties</propertiesFile>\n			<storageEndpoint>s3.amazonaws.com</storageEndpoint>\n			<storagePortNumber>80</storagePortNumber>\n			<storageVirtualPath></storageVirtualPath>\n		</connectionFactory>\n	</storageInterface>');
-- INSERT INTO "APPLICATION"."IAAS_CONFIGURATION" (IAAS_SERVICE_NAME,DESCRIPTION,SERVICE_INTERFACE_XML,INTERNET_DNS_XML,STORAGE_INTERFACE_XML) VALUES ('AWS','Amazon Web Services','<serviceInterface xsi:type="awsServiceInterface">\n	<timeout>300000</timeout>\n	<connectionFactory>\n		<propertiesFile>aws.properties</propertiesFile>\n	</connectionFactory>\n</serviceInterface>','<internetDnsClient xsi:type="route53DnsClient">\n	<propFile>/usr/local/rtws/master/conf/aws.properties</propFile>\n	<queryUrl>https://route53.amazonaws.com/2011-05-05/change/</queryUrl>\n	<changeUrl>https://route53.amazonaws.com/2011-05-05/hostedzone/RTWS_HOSTED_ZONE/rrset</changeUrl>\n</internetDnsClient>','<storageInterface xsi:type="jetSetStorageService">\n		<connectionFactory>\n			<propertiesFile>aws.properties</propertiesFile>\n			<storageEndpoint>s3.amazonaws.com</storageEndpoint>\n			<storagePortNumber>80</storagePortNumber>\n			<storageVirtualPath></storageVirtualPath>\n		</connectionFactory>\n	</storageInterface>');

INSERT INTO APPLICATION.AVAILABILITY_ZONES SELECT * FROM CSVREAD('/usr/local/rtws/system-database/tms/availability_zones.csv');

-- TMS/Gateway populates these tables at startup
-- INSERT INTO APPLICATION.SOFTWARE_RELEASES SELECT * FROM CSVREAD('/usr/local/rtws/system-database/tms/software_releases.csv');
-- INSERT INTO APPLICATION.MACHINE_IMAGES SELECT * FROM CSVREAD('/usr/local/rtws/system-database/tms/machine_images.csv');

INSERT INTO APPLICATION.SYSTEM_SIZING SELECT * FROM CSVREAD('/usr/local/rtws/system-database/tms/system_sizing.csv');

INSERT INTO APPLICATION.INSTANCE_TYPES SELECT * FROM CSVREAD('/usr/local/rtws/system-database/tms/instance_types.csv');

INSERT INTO APPLICATION.TENANT_ACCOUNT_ACCESS SELECT * FROM CSVREAD('/usr/local/rtws/system-database/euca-tms/tenant_account_access.csv');

commit ;

INSERT INTO APPLICATION.PROCESS_GROUP_CONFIG SELECT * FROM CSVREAD('/usr/local/rtws/system-database/tms/process_group_config.csv');

INSERT INTO APPLICATION.DATASINK_CONFIG SELECT * FROM CSVREAD('/usr/local/rtws/system-database/euca-tms/datasink_config.csv');

INSERT INTO APPLICATION.WEBAPPS_CONFIG SELECT * FROM CSVREAD('/usr/local/rtws/system-database/tms/webapps_config.csv');


commit ;