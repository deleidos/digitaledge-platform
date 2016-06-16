CREATE TABLE IF NOT EXISTS APPLICATION.SYSTEM_USAGE (
	 ACCOUNT_ID						NUMBER NOT NULL,
	 WBS_CODE						VARCHAR(512),
	 DOMAIN							VARCHAR(512) NOT NULL,
	 SNAPSHOT				        TIMESTAMP NOT NULL,
	 ALLOCATED_CORES				NUMBER NOT NULL,	 
	 ALLOCATED_EBS					FLOAT NOT NULL DEFAULT 0.0,
	 FOREIGN KEY (ACCOUNT_ID) REFERENCES APPLICATION.IAAS_ACCOUNTS(ACCOUNT_ID),
	 PRIMARY KEY (SNAPSHOT,DOMAIN)
);

CREATE TABLE IF NOT EXISTS APPLICATION.SYSTEM_USAGE_SUMMARY (
	 ACCOUNT_ID						NUMBER NOT NULL,
	 WBS_CODE						VARCHAR(512),
	 DOMAIN							VARCHAR(512) NOT NULL,
	 DAY							DATE NOT NULL,
	 HOUR							NUMBER NOT NULL,
	 ALLOCATED_CORES				NUMBER NOT NULL,
	 ALLOCATED_EBS					FLOAT NOT NULL DEFAULT 0.0,
	 FOREIGN KEY (ACCOUNT_ID) REFERENCES APPLICATION.IAAS_ACCOUNTS(ACCOUNT_ID)
);



insert into APPLICATION.IAAS_ACCOUNTS
	(ACCOUNT_ID,ACCOUNT_NAME,DESCRIPTION,IAAS_SERVICE_NAME,IAAS_SERVICE_NUMBER,DOMAIN_NAME_SUFFIX,KEYPAIR_NAME,AWS_ACCOUNT_EMAIL)
		values (999,'joe','a','a','a','a','a','a');

-- Simulate 2 system running and reports are received every 15 minutes

-- Master Starts
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test.domain',DATEADD('day',-1,current_timestamp()),
			1,3);
		
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test2.domain',DATEADD('day',-1,current_timestamp()),
			1,3);
		
-- Other Process Groups Allocated/Dealocated		
-- Hour 1
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test.domain',DATEADD('minute',15,DATEADD('day',-1,current_timestamp())),
			3,3);
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test.domain',DATEADD('minute',30,DATEADD('day',-1,current_timestamp())),
		6,3);
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test.domain',DATEADD('minute',45,DATEADD('day',-1,current_timestamp())),
		12,3);
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test.domain',DATEADD('minute',60,DATEADD('day',-1,current_timestamp())),
		6,3);


insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test2.domain',DATEADD('minute',15,DATEADD('day',-1,current_timestamp())),
		3,3);
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test2.domain',DATEADD('minute',30,DATEADD('day',-1,current_timestamp())),
		9,3);
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test2.domain',DATEADD('minute',45,DATEADD('day',-1,current_timestamp())),
		12,3);
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test2.domain',DATEADD('minute',60,DATEADD('day',-1,current_timestamp())),
		24,3);
		
-- Hour 2
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test.domain',DATEADD('minute',75,DATEADD('day',-1,current_timestamp())),
		6,3);
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test.domain',DATEADD('minute',90,DATEADD('day',-1,current_timestamp())),
		24,3);
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test.domain',DATEADD('minute',105,DATEADD('day',-1,current_timestamp())),
		69,3);
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test.domain',DATEADD('minute',120,DATEADD('day',-1,current_timestamp())),
		6,3);

insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test2.domain',DATEADD('minute',75,DATEADD('day',-1,current_timestamp())),
		65,3);
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test2.domain',DATEADD('minute',90,DATEADD('day',-1,current_timestamp())),
		24,3);
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test2.domain',DATEADD('minute',105,DATEADD('day',-1,current_timestamp())),
		12,3);
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test2.domain',DATEADD('minute',120,DATEADD('day',-1,current_timestamp())),
		6,3);

		
-- Day 2 Hour 1  (test2.domain has been shutdown)
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test.domain',
			DATEADD('day',-1,DATEADD('minute',15,DATEADD('day',-1,current_timestamp()))),
			6,3);
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test.domain',
			DATEADD('day',-1,DATEADD('minute',30,DATEADD('day',-1,current_timestamp()))),
			24,3);
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test.domain',
			DATEADD('day',-1,DATEADD('minute',45,DATEADD('day',-1,current_timestamp()))),
			12,3);
insert into application.SYSTEM_USAGE
	(ACCOUNT_ID,DOMAIN,SNAPSHOT,ALLOCATED_CORES,ALLOCATED_EBS)
		values (999,'test.domain',
			DATEADD('day',-1,DATEADD('minute',60,DATEADD('day',-1,current_timestamp()))),
			6,3);