CREATE TABLE IF NOT EXISTS "DIMENSIONS"."BASEBALLTEAMS"
(
   TEAMKEY tinyint PRIMARY KEY NOT NULL,
   TEAMSHORT varchar(3) NOT NULL,
   LEAGUE varchar(1) NOT NULL,
   TEAMCITYREGION varchar(64) NOT NULL,
   TEAMNAME varchar(64) NOT NULL,
   TEAMOWNER varchar(72) NOT NULL,
   MANAGER varchar(72)
);

GRANT ALL ON "DIMENSIONS"."BASEBALLTEAMS" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."FIPS6_COUNTY"
(
   FIPS6_STATE_CODE varchar(3) NOT NULL,
   FIPS6_COUNTY_CODE varchar(3) NOT NULL,
   COUNTY_NAME varchar(30) NOT NULL,
   CONSTRAINT CONSTRAINT_F PRIMARY KEY (FIPS6_STATE_CODE,FIPS6_COUNTY_CODE)
);

GRANT ALL ON "DIMENSIONS"."FIPS6_COUNTY" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."FIPS6_STATE"
(
   FIPS6_STATE_CODE varchar(3) PRIMARY KEY NOT NULL,
   STATE_NAME varchar(20) NOT NULL
);

GRANT ALL ON "DIMENSIONS"."FIPS6_STATE" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."RITA_OTP_SUMMARY"
(
   UNIQUE_CARRIER varchar(10) NOT NULL,
   YEAR integer NOT NULL,
   MONTH integer NOT NULL,
   DEP_DELAY double NOT NULL,
   ARR_DELAY double NOT NULL
);

GRANT ALL ON "DIMENSIONS"."RITA_OTP_SUMMARY" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."ORIGIN2AIRPORT"
(
   CODE varchar(3),
   NAME varchar(100),
   STATE varchar(2),
   CITY varchar(100),
   CITY_ABR varchar(20),
   NAME_SELECT varchar(0),
   LATITUDE decimal(65535,32767),
   LONGITUDE decimal(65535,32767)
);

GRANT ALL ON "DIMENSIONS"."ORIGIN2AIRPORT" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."DEST2AIRPORT"
(
   CODE varchar(3),
   NAME varchar(100),
   STATE varchar(2),
   CITY varchar(100),
   CITY_ABR varchar(20),
   NAME_SELECT varchar(0),
   LATITUDE decimal(65535,32767),
   LONGITUDE decimal(65535,32767)
);

GRANT ALL ON "DIMENSIONS"."DEST2AIRPORT" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."ORDER_INFO"
(
   OI_ORDERKEY decimal(65535,32767),
   OI_STATUS varchar(1) NOT NULL,
   OI_PRIORITY varchar(15) NOT NULL
);

CREATE UNIQUE INDEX ORDER_INFO_NK_INDEX_4 ON "DIMENSIONS"."ORDER_INFO"
(
  OI_STATUS,
  OI_PRIORITY
);

GRANT ALL ON "DIMENSIONS"."ORDER_INFO" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."SHIPMENT_INFO"
(
   SI_SHIPMENTKEY decimal(65535,32767),
   SI_PRIORITY varchar(15) NOT NULL,
   SI_INSTRUCTIONS varchar(25) NOT NULL,
   SI_MODE varchar(10) NOT NULL
);

CREATE UNIQUE INDEX SHIPMENT_INFO_NK_INDEX_9 ON "DIMENSIONS"."SHIPMENT_INFO"
(
  SI_PRIORITY,
  SI_INSTRUCTIONS,
  SI_MODE
);

GRANT ALL ON "DIMENSIONS"."SHIPMENT_INFO" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."CUSTOMER"
(
   C_CUSTKEY decimal(65535,32767) PRIMARY KEY NOT NULL,
   C_NAME varchar(25) NOT NULL,
   C_ADDRESS varchar(40),
   C_NATIONKEY decimal(65535,32767),
   C_PHONE varchar(15),
   C_ACCTBAL decimal(65535,32767),
   C_MKTSEGMENT varchar(10),
   C_COMMENT varchar(117),
   C_SSN_4 varchar(4)
);

CREATE UNIQUE INDEX CUSTOMER_NK ON "DIMENSIONS"."CUSTOMER"(C_NAME);

CREATE UNIQUE INDEX PRIMARY_KEY_A7 ON "DIMENSIONS"."CUSTOMER"(C_CUSTKEY);

CREATE INDEX TESTINDEX ON "DIMENSIONS"."CUSTOMER"
(
  C_CUSTKEY,
  C_NAME,
  C_ADDRESS,
  C_NATIONKEY,
  C_ACCTBAL,
  C_MKTSEGMENT,
  C_COMMENT,
  C_PHONE
);

GRANT ALL ON "DIMENSIONS"."CUSTOMER" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."NATION"
(
   N_NATIONKEY decimal(65535,32767) PRIMARY KEY NOT NULL,
   N_NAME varchar(25) NOT NULL,
   N_REGIONKEY decimal(65535,32767),
   N_COMMENT varchar(152)
);

CREATE UNIQUE INDEX PRIMARY_KEY_88 ON "DIMENSIONS"."NATION"(N_NATIONKEY);

CREATE UNIQUE INDEX NATION_PK ON "DIMENSIONS"."NATION"(N_NATIONKEY);

GRANT ALL ON "DIMENSIONS"."NATION" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."PART"
(
   P_PARTKEY decimal(65535,32767) PRIMARY KEY NOT NULL,
   P_NAME varchar(55) NOT NULL,
   P_MFGR varchar(25),
   P_BRAND varchar(10),
   P_TYPE varchar(25),
   P_SIZE decimal(65535,32767),
   P_CONTAINER varchar(10),
   P_RETAILPRICE decimal(65535,32767),
   P_COMMENT varchar(23)
);

CREATE UNIQUE INDEX PART_NK ON "DIMENSIONS"."PART"(P_NAME);

CREATE UNIQUE INDEX PRIMARY_KEY_25 ON "DIMENSIONS"."PART"(P_PARTKEY);

GRANT ALL ON "DIMENSIONS"."PART" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."SUPPLIER"
(
   S_SUPPKEY decimal(65535,32767) PRIMARY KEY NOT NULL,
   S_NAME varchar(25) NOT NULL,
   S_ADDRESS varchar(40),
   S_NATIONKEY decimal(65535,32767),
   S_PHONE varchar(15),
   S_ACCTBAL decimal(65535,32767),
   S_COMMENT varchar(101),
   S_TESTCOLUMN varchar(2147483647)
);

CREATE UNIQUE INDEX PRIMARY_KEY_EB8 ON "DIMENSIONS"."SUPPLIER"(S_SUPPKEY);

CREATE UNIQUE INDEX SUPPLIER_NK ON "DIMENSIONS"."SUPPLIER"(S_NAME);

GRANT ALL ON "DIMENSIONS"."SUPPLIER" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."EVENT_TYPE"
(
   EVENT_TYPE_KEY decimal(65535,32767) PRIMARY KEY NOT NULL,
   EVENT_TYPE varchar(25) NOT NULL
);

CREATE UNIQUE INDEX EVENT_TYPE_NK ON "DIMENSIONS"."EVENT_TYPE"(EVENT_TYPE);

GRANT ALL ON "DIMENSIONS"."EVENT_TYPE" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."OBJECT"
(
   OBJECT_KEY decimal(65535,32767) PRIMARY KEY NOT NULL,
   OBJECT_TYPE varchar(25) NOT NULL,
   OBJECT_ID_NAME varchar(25) NOT NULL,
   OBJECT_ID_VALUE varchar(25) NOT NULL
);

CREATE UNIQUE INDEX OBJECT_NK ON "DIMENSIONS"."OBJECT"
(
  OBJECT_TYPE,
  OBJECT_ID_NAME,
  OBJECT_ID_VALUE
);

GRANT ALL ON "DIMENSIONS"."OBJECT" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."CANCELLATION"
(
   CODE varchar(1) PRIMARY KEY NOT NULL,
   DESCRIPTION varchar(100)
);

GRANT ALL ON "DIMENSIONS"."CANCELLATION" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."UNIQUE_CARRIERS"
(
   CODE varchar(8) PRIMARY KEY NOT NULL,
   DESCRIPTION varchar(100)
);

GRANT ALL ON "DIMENSIONS"."UNIQUE_CARRIERS" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."AIRLINE_ID"
(
   CODE varchar(5) PRIMARY KEY NOT NULL,
   DESCRIPTION varchar(100)
);

GRANT ALL ON "DIMENSIONS"."AIRLINE_ID" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."ROUTES"
(
   ROUTE_KEY decimal(65535,32767),
   ROUTE_TAG varchar(10) PRIMARY KEY NOT NULL,
   ROUTE_DESCRIPTION varchar(60)
);

GRANT ALL ON "DIMENSIONS"."ROUTES" TO INGEST;

--=====================================================================================================
--=====================================================================================================

CREATE TABLE IF NOT EXISTS APPLICATION.SCHEDULED_TASKS(
	id long not null AUTO_INCREMENT primary key,
	system varchar(1024) not null,
	processGroup varchar(512) NOT NULL,
	numNodes integer NOT NULL,
	scriptName varchar(512) NOT NULL,
	arguments varchar(1024),
	triggerCron varchar(1024) NOT NULL
);

GRANT ALL ON APPLICATION.SCHEDULED_TASKS TO APPUSER;

alter table APPLICATION.SCHEDULED_TASKS ADD UNIQUE(SYSTEM,PROCESSGROUP,NUMNODES,SCRIPTNAME,ARGUMENTS,TRIGGERCRON);
