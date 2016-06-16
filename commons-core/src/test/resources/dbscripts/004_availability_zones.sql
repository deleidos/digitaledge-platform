CREATE TABLE IF NOT EXISTS "APPLICATION"."AVAILABILITY_ZONES"
(
   IAAS_SERVICE_NAME varchar(64) NOT NULL,
   IAAS_REGION varchar(64) NOT NULL,
   IAAS_AZONE varchar(64) NOT NULL,
   IAAS_SW_BUCKET varchar(32),
   DESCRIPTION varchar(512),
   PROPERTIES_FILE varchar(64) NOT NULL,
   STORAGE_ENDPOINT varchar(256) NOT NULL,
   SERVICE_ENDPOINT varchar(256) NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS AZ_NAMING_INDEX_E ON "APPLICATION"."AVAILABILITY_ZONES"
(
  IAAS_SERVICE_NAME,
  IAAS_REGION,
  IAAS_AZONE
);



INSERT INTO "APPLICATION"."AVAILABILITY_ZONES" (IAAS_SERVICE_NAME,IAAS_REGION,IAAS_AZONE,IAAS_SW_BUCKET,DESCRIPTION,PROPERTIES_FILE,STORAGE_ENDPOINT,SERVICE_ENDPOINT) VALUES ('AWS','us-east-1','us-east-1a','rtws.account.3.appfs.us-east-1','Amazon Web Services','/usr/local/rtws/conf/aws.properties','http://s3.amazonaws.com:80','http://ec2.us-east-1.amazonaws.com:80');
INSERT INTO "APPLICATION"."AVAILABILITY_ZONES" (IAAS_SERVICE_NAME,IAAS_REGION,IAAS_AZONE,IAAS_SW_BUCKET,DESCRIPTION,PROPERTIES_FILE,STORAGE_ENDPOINT,SERVICE_ENDPOINT) VALUES ('AWS','us-east-1','us-east-1b','rtws.account.3.appfs.us-east-1','Amazon Web Services','/usr/local/rtws/conf/aws.properties','http://s3.amazonaws.com:80','http://ec2.us-east-1.amazonaws.com:80');
INSERT INTO "APPLICATION"."AVAILABILITY_ZONES" (IAAS_SERVICE_NAME,IAAS_REGION,IAAS_AZONE,IAAS_SW_BUCKET,DESCRIPTION,PROPERTIES_FILE,STORAGE_ENDPOINT,SERVICE_ENDPOINT) VALUES ('AWS','us-east-1','us-east-1c','rtws.account.3.appfs.us-east-1','Amazon Web Services','/usr/local/rtws/conf/aws.properties','http://s3.amazonaws.com:80','http://ec2.us-east-1.amazonaws.com:80');
INSERT INTO "APPLICATION"."AVAILABILITY_ZONES" (IAAS_SERVICE_NAME,IAAS_REGION,IAAS_AZONE,IAAS_SW_BUCKET,DESCRIPTION,PROPERTIES_FILE,STORAGE_ENDPOINT,SERVICE_ENDPOINT) VALUES ('AWS','us-east-1','us-east-1d','rtws.account.3.appfs.us-east-1','Amazon Web Services','/usr/local/rtws/conf/aws.properties','http://s3.amazonaws.com:80','http://ec2.us-east-1.amazonaws.com:80');
INSERT INTO "APPLICATION"."AVAILABILITY_ZONES" (IAAS_SERVICE_NAME,IAAS_REGION,IAAS_AZONE,IAAS_SW_BUCKET,DESCRIPTION,PROPERTIES_FILE,STORAGE_ENDPOINT,SERVICE_ENDPOINT) VALUES ('AWS','us-east-1','us-east-1e','rtws.account.3.appfs.us-east-1','Amazon Web Services','/usr/local/rtws/conf/aws.properties','http://s3.amazonaws.com:80','http://ec2.us-east-1.amazonaws.com:80');
INSERT INTO "APPLICATION"."AVAILABILITY_ZONES" (IAAS_SERVICE_NAME,IAAS_REGION,IAAS_AZONE,IAAS_SW_BUCKET,DESCRIPTION,PROPERTIES_FILE,STORAGE_ENDPOINT,SERVICE_ENDPOINT) VALUES ('AWS','us-west-1','us-west-1a','rtws.account.3.appfs.us-east-1','Amazon Web Services','/usr/local/rtws/conf/aws.properties','http://s3.amazonaws.com:80','http://ec2.us-west-1.amazonaws.com:80');
INSERT INTO "APPLICATION"."AVAILABILITY_ZONES" (IAAS_SERVICE_NAME,IAAS_REGION,IAAS_AZONE,IAAS_SW_BUCKET,DESCRIPTION,PROPERTIES_FILE,STORAGE_ENDPOINT,SERVICE_ENDPOINT) VALUES ('AWS','us-west-1','us-west-1b','rtws.account.3.appfs.us-east-1','Amazon Web Services','/usr/local/rtws/conf/aws.properties','http://s3.amazonaws.com:80','http://ec2.us-west-1.amazonaws.com:80');
INSERT INTO "APPLICATION"."AVAILABILITY_ZONES" (IAAS_SERVICE_NAME,IAAS_REGION,IAAS_AZONE,IAAS_SW_BUCKET,DESCRIPTION,PROPERTIES_FILE,STORAGE_ENDPOINT,SERVICE_ENDPOINT) VALUES ('AWS','us-west-1','us-west-1c','rtws.account.3.appfs.us-east-1','Amazon Web Services','/usr/local/rtws/conf/aws.properties','http://s3.amazonaws.com:80','http://ec2.us-west-1.amazonaws.com:80');
INSERT INTO "APPLICATION"."AVAILABILITY_ZONES" (IAAS_SERVICE_NAME,IAAS_REGION,IAAS_AZONE,IAAS_SW_BUCKET,DESCRIPTION,PROPERTIES_FILE,STORAGE_ENDPOINT,SERVICE_ENDPOINT) VALUES ('AWS','us-west-2','us-west-2a','rtws.account.3.appfs.us-east-1','Amazon Web Services','/usr/local/rtws/conf/aws.properties','http://s3.amazonaws.com:80','http://ec2.us-west-2.amazonaws.com:80');
INSERT INTO "APPLICATION"."AVAILABILITY_ZONES" (IAAS_SERVICE_NAME,IAAS_REGION,IAAS_AZONE,IAAS_SW_BUCKET,DESCRIPTION,PROPERTIES_FILE,STORAGE_ENDPOINT,SERVICE_ENDPOINT) VALUES ('AWS','us-west-2','us-west-2b','rtws.account.3.appfs.us-east-1','Amazon Web Services','/usr/local/rtws/conf/aws.properties','http://s3.amazonaws.com:80','http://ec2.us-west-2.amazonaws.com:80');

UPDATE COMMONDB.APPLICATION.AVAILABILITY_ZONES SET IAAS_SW_BUCKET='rtws.account.3.appfs.us-east-1' WHERE IAAS_REGION = 'us-east-1';
UPDATE COMMONDB.APPLICATION.AVAILABILITY_ZONES SET IAAS_SW_BUCKET='rtws.account.3.appfs.us-west-1' WHERE IAAS_REGION like '%us-west-1%';
UPDATE COMMONDB.APPLICATION.AVAILABILITY_ZONES SET IAAS_SW_BUCKET='rtws.account.3.appfs.us-west-2' WHERE IAAS_REGION like '%us-west-2%';

