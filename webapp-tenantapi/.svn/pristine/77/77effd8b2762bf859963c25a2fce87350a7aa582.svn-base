CREATE TABLE IF NOT EXISTS APPLICATION.PROCESS_GROUP_CONFIG(
    PROCESS_GROUP_NAME		VARCHAR2(255)	NOT NULL,
    SECURITY_GROUP			VARCHAR2(255)	NOT NULL,
    INTERNAL_DOMAIN_NAME	VARCHAR2(255)	NOT NULL,
    MANAGEMENT_INTERFACES	VARCHAR2(255),
    PUBLIC_DOMAIN_NAME		VARCHAR2(255),
    DEFAULT_INSTANCE_TYPE	VARCHAR2(32)	NOT NULL,
    INSTANCE_STORAGE		VARCHAR2(64)	NOT NULL,
    FIXED_WEBAPPS			VARCHAR2(1024),
    MANIFEST_FILENAME		VARCHAR2(255)	NOT NULL,
    INGEST_CONFIG_FILENAME	VARCHAR2(255),
    CONFIG_PERMISSIONS		VARCHAR2(4000)  NOT NULL,
    OWNER					VARCHAR2(255) NOT NULL,
    VISIBILITY				VARCHAR2(8) NOT NULL,
    CONTAINER_PORT_EXPOSE	VARCHAR2(255),
    CONTAINER_PORT_EXPOSE_HOST	VARCHAR2(255),
	SERVICE_ENABLED         BOOLEAN,
    PRIMARY KEY (PROCESS_GROUP_NAME,OWNER),
    FOREIGN KEY (OWNER) REFERENCES APPLICATION.TENANT_ACCOUNT_ACCESS(TENANT_ID)
);

INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS,OWNER,VISIBILITY) VALUES ('jms.external','internal.default','jms-ext-node?.@build.domain@','org.apache.activemq','jms-ext?.@build-domain@','m1.large','instance','','activemq.ini','','{"default-num-volumes" : 2, "default-volume-size" : 15, "config-volume-size" : true,  "config-persistent-ip" : true,  "config-instance-size" : false, "config-min-max" : false, "config-scaling" : false, "config-jms-persistence" : false }','admin','public');
INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS,OWNER,VISIBILITY) VALUES ('jms.internal','internal.default','jms-int-node?.@build.domain@','org.apache.activemq','','m1.large','instance','','activemq.ini','','{"default-num-volumes" : 2, "default-volume-size" : 15, "config-volume-size" : true,  "config-persistent-ip" : true,  "config-instance-size" : false, "config-min-max" : false, "config-scaling" : false, "config-jms-persistence" : true  }','admin','public');
INSERT INTO "APPLICATION"."PROCESS_GROUP_CONFIG" (PROCESS_GROUP_NAME,SECURITY_GROUP,INTERNAL_DOMAIN_NAME,MANAGEMENT_INTERFACES,PUBLIC_DOMAIN_NAME,DEFAULT_INSTANCE_TYPE,INSTANCE_STORAGE,FIXED_WEBAPPS,MANIFEST_FILENAME,INGEST_CONFIG_FILENAME,CONFIG_PERMISSIONS,OWNER,VISIBILITY) VALUES ('master','internal.default','master.@build.domain@','','','m1.small','permanent','','master.ini','','{"default-num-volumes" : 0, "default-volume-size" : 0,  "config-volume-size" : false, "config-persistent-ip" : false, "config-instance-size" : false, "config-min-max" : false, "config-scaling" : false, "config-jms-persistence" : false }','admin','public');