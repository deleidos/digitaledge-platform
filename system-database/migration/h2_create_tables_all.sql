  CREATE TABLE DIMENSIONS.AIRLINE_ID
   (	"CODE" VARCHAR2(5),                                                        
	"DESCRIPTION" VARCHAR2(100),                                                   
	 CONSTRAINT "AIRLINE_ID_PK" PRIMARY KEY ("CODE")                               
   );
                                                                                
  CREATE TABLE DIMENSIONS.AIRPORTS_OLD
   (	"CODE" VARCHAR2(3),                                                        
	"DESCRIPTION" VARCHAR2(100),                                                   
	 CONSTRAINT "AIRPORT_PK" PRIMARY KEY ("CODE")                                  
   );
                                                                                
  CREATE TABLE DIMENSIONS.CANCELLATION
   (	"CODE" CHAR(1),                                                            
	"DESCRIPTION" VARCHAR2(100),                                                   
	 CONSTRAINT "CANCELLATION_PK" PRIMARY KEY ("CODE")                             
   );
                                                                                
  CREATE TABLE DIMENSIONS.DIVERSIONS
   (	"CODE" NUMBER,                                                             
	"DESCRIPTION" VARCHAR2(100),                                                   
	 CONSTRAINT "DIVERSIONS_PK" PRIMARY KEY ("CODE")                               
   );
                                                                                
  CREATE TABLE DIMENSIONS.MONTHS
   (	"CODE" NUMBER,                                                             
	"DESCRIPTION" VARCHAR2(10),                                                    
	 CONSTRAINT "MONTHS_PK" PRIMARY KEY ("CODE")                                   
   );
                                                                                
  CREATE TABLE DIMENSIONS.STATE_ABR
   (	"CODE" VARCHAR2(2),                                                        
	"DESCRIPTION" VARCHAR2(50),                                                    
	 CONSTRAINT "STATE_ABR_PK" PRIMARY KEY ("CODE")                                
   );
                                                                                
  CREATE TABLE DIMENSIONS.WEEKDAYS
   (	"CODE" NUMBER,                                                             
	"DESCRIPTION" VARCHAR2(10),                                                    
	 CONSTRAINT "WEEKDAYS_PK" PRIMARY KEY ("CODE")                                 
   );
                                                                                
  CREATE TABLE DIMENSIONS.YESNO_RESP
   (	"CODE" NUMBER,                                                             
	"DESCRIPTION" VARCHAR2(5),                                                     
	 CONSTRAINT "YESNO_RESP_PK" PRIMARY KEY ("CODE")                               
   );
                                                                                
  CREATE TABLE DIMENSIONS.NATION
   (	"N_NATIONKEY" NUMBER,                                 
	"N_NAME" VARCHAR2(25) NOT NULL,                                         
	"N_REGIONKEY" NUMBER,                                     
	"N_COMMENT" VARCHAR2(152),                                                     
	 CONSTRAINT "NATION_PK" PRIMARY KEY ("N_NATIONKEY")                            
   );
                                                                                
  CREATE TABLE DIMENSIONS.REGION
   (	"R_REGIONKEY" NUMBER,                                 
	"R_NAME" VARCHAR2(25) NOT NULL,                                         
	"R_COMMENT" VARCHAR2(152),                                                     
	 CONSTRAINT "REGION_PK" PRIMARY KEY ("R_REGIONKEY")                            
   );
                                                                                
  CREATE TABLE DIMENSIONS.PART
   (	"P_PARTKEY" NUMBER,                                   
	"P_NAME" VARCHAR2(55) NOT NULL,                                         
	"P_MFGR" VARCHAR2(25),                                                         
	"P_BRAND" VARCHAR2(10),                                                        
	"P_TYPE" VARCHAR2(25),                                                         
	"P_SIZE" NUMBER,                                          
	"P_CONTAINER" VARCHAR2(10),                                                    
	"P_RETAILPRICE" NUMBER,                                  
	"P_COMMENT" VARCHAR2(23),                                                      
	 CONSTRAINT "PART_PK" PRIMARY KEY ("P_PARTKEY")                                
   );
                                                                                
  CREATE TABLE DIMENSIONS.SUPPLIER
   (	"S_SUPPKEY" NUMBER,                                   
	"S_NAME" VARCHAR2(25) NOT NULL,                                         
	"S_ADDRESS" VARCHAR2(40),                                                      
	"S_NATIONKEY" NUMBER,                                     
	"S_PHONE" VARCHAR2(15),                                                        
	"S_ACCTBAL" NUMBER,                                      
	"S_COMMENT" VARCHAR2(101),                                                     
	 CONSTRAINT "SUPPLIER_PK" PRIMARY KEY ("S_SUPPKEY")                            
   );
                                                                                
  CREATE TABLE DIMENSIONS.PARTSUPP
   (	"PS_PARTKEY" NUMBER,                                  
	"PS_SUPPKEY" NUMBER,                                      
	"PS_AVAILQTY" NUMBER,                                     
	"PS_SUPPLYCOST" NUMBER,                                  
	"PS_COMMENT" VARCHAR2(199) NOT NULL,                                    
	 CONSTRAINT "PARTSUPP_PK" PRIMARY KEY ("PS_PARTKEY", "PS_SUPPKEY")             
   );
                                                                                
  CREATE TABLE DIMENSIONS.CUSTOMER
   (	"C_CUSTKEY" NUMBER,                                   
	"C_NAME" VARCHAR2(25) NOT NULL,                                         
	"C_ADDRESS" VARCHAR2(40),                                                      
	"C_NATIONKEY" NUMBER,                                     
	"C_PHONE" VARCHAR2(15),                                                        
	"C_ACCTBAL" NUMBER,                                      
	"C_MKTSEGMENT" VARCHAR2(10),                                                   
	"C_COMMENT" VARCHAR2(117),                                                     
	 CONSTRAINT "CUSTOMER_PK" PRIMARY KEY ("C_CUSTKEY")                            
   );
                                                                                
  CREATE TABLE DIMENSIONS.ORDERS
   (	"O_ORDERKEY" NUMBER,                                  
	"O_CUSTKEY" NUMBER,                                       
	"O_ORDERSTATUS" CHAR(1) NOT NULL,                                       
	"O_TOTALPRICE" NUMBER,                                   
	"O_ORDERDATE" DATE NOT NULL,                                            
	"O_ORDERPRIORITY" VARCHAR2(15) NOT NULL,                                
	"O_CLERK" VARCHAR2(15) NOT NULL,                                        
	"O_SHIPPRIORITY" NUMBER,                                  
	"O_COMMENT" VARCHAR2(79) NOT NULL,                                      
	 CONSTRAINT "ORDERS_PK" PRIMARY KEY ("O_ORDERKEY")                             
   );
                                                                                
  CREATE TABLE DIMENSIONS.LINEITEM
   (	"L_ORDERKEY" NUMBER NOT NULL,                                  
	"L_PARTKEY" NUMBER NOT NULL,                                       
	"L_SUPPKEY" NUMBER NOT NULL,                                       
	"L_LINENUMBER" NUMBER NOT NULL,                                    
	"L_QUANTITY" NUMBER(15,2) NOT NULL,                                     
	"L_EXTENDEDPRICE" NUMBER(15,2) NOT NULL,                                
	"L_DISCOUNT" NUMBER(15,2) NOT NULL,                                     
	"L_TAX" NUMBER(15,2) NOT NULL,                                          
	"L_RETURNFLAG" CHAR(1) NOT NULL,                                        
	"L_LINESTATUS" CHAR(1) NOT NULL,                                        
	"L_SHIPDATE" DATE NOT NULL,                                             
	"L_COMMITDATE" DATE NOT NULL,                                           
	"L_RECEIPTDATE" DATE NOT NULL,                                          
	"L_SHIPINSTRUCT" VARCHAR2(25) NOT NULL,                                 
	"L_SHIPMODE" VARCHAR2(10) NOT NULL,                                     
	"L_COMMENT" VARCHAR2(44) NOT NULL,                                      
	 CONSTRAINT "LINEITEM_PK" PRIMARY KEY ("L_ORDERKEY", "L_LINENUMBER")           
   );
                                                                                
  CREATE TABLE DIMENSIONS.ROUTES
   (	"ROUTE_KEY" NUMBER,                                                        
	"ROUTE_TAG" VARCHAR2(10),                                                      
	"ROUTE_DESCRIPTION" VARCHAR2(60),                                              
	 CONSTRAINT "ROUTES_PK" PRIMARY KEY ("ROUTE_TAG")                              
   );
                                                                                
  CREATE TABLE DIMENSIONS.STOPS
   (	"STOPS_KEY" NUMBER,                                                        
	"DESCRIPTION" VARCHAR2(80),                                                    
	"ROUTE_TAG" VARCHAR2(10),                                                      
	"STOP_ID" NUMBER,                                                              
	"STOP_TAG" VARCHAR2(20),                                                       
	"LATITUDE" NUMBER,                                                        
	"LONGITUDE" NUMBER,                                                       
	 CONSTRAINT "STOPS_PK" PRIMARY KEY ("ROUTE_TAG", "STOP_ID")                    
   );
                                                                                
  CREATE TABLE DIMENSIONS.ZIPCODE_LOCATIONS
   (	"ZIPCODE_LOCATIONS_KEY" NUMBER,                                            
	"ZIP_CODE" VARCHAR2(10),                                                       
	"STATE_ABR" VARCHAR2(3),                                                       
	"LATITUDE" NUMBER,                                                             
	"LONGITUDE" NUMBER,                                                            
	"CITY" VARCHAR2(40),                                                           
	"STATE" VARCHAR2(30),                                                          
	 CONSTRAINT "ZIPCODE_LOCATIONS_PK" PRIMARY KEY ("ZIP_CODE")                    
   );
                                                                                
  CREATE TABLE DIMENSIONS.ORDER_INFO
   (	"OI_ORDERKEY" NUMBER,                                
	"OI_STATUS" VARCHAR2(1) NOT NULL,                                       
	"OI_PRIORITY" VARCHAR2(15) NOT NULL,                                    
	 CONSTRAINT "ORDER_INFO_NK" UNIQUE ("OI_STATUS", "OI_PRIORITY")                
   );
                                                                                
  CREATE TABLE DIMENSIONS.SHIPMENT_INFO
   (	"SI_SHIPMENTKEY" NUMBER,                             
	"SI_PRIORITY" VARCHAR2(15) NOT NULL,                                    
	"SI_INSTRUCTIONS" VARCHAR2(25) NOT NULL,                                
	"SI_MODE" VARCHAR2(10) NOT NULL,                                        
	 CONSTRAINT "SHIPMENT_INFO_NK" UNIQUE ("SI_PRIORITY", "SI_INSTRUCTIONS", "SI_MODE")
   );
                                                                                
  CREATE TABLE DIMENSIONS.EVENT_TYPE
   (	"EVENT_TYPE_KEY" NUMBER,                                   
	"EVENT_TYPE" VARCHAR2(25) NOT NULL,                                     
	 CONSTRAINT "EVENT_TYPE_PK" PRIMARY KEY ("EVENT_TYPE_KEY")                     
   );
                                                                                
  CREATE TABLE DIMENSIONS.OBJECT
   (	"OBJECT_KEY" NUMBER,                                       
	"OBJECT_TYPE" VARCHAR2(25) NOT NULL,                                    
	"OBJECT_ID_NAME" VARCHAR2(25) NOT NULL,                                 
	"OBJECT_ID_VALUE" VARCHAR2(25) NOT NULL,                                
	 CONSTRAINT "OBJECT_PK" PRIMARY KEY ("OBJECT_KEY")                             
   );
                                                                                
  CREATE TABLE DIMENSIONS.DATE_DIM
   (	"DATE_KEY" NUMBER,                                    
	"FULL_DATE" DATE,                                                              
	"DAY_NUM_OF_WEEK" NUMBER,                                                 
	"DAY_NUM_OF_MONTH" NUMBER,                                                
	"DAY_NUM_OF_QUARTER" NUMBER,                                              
	"DAY_NUM_OF_YEAR" NUMBER,                                                 
	"DAY_NUM_ABSOLUTE" NUMBER,                                                
	"DAY_OF_WEEK_NAME" VARCHAR2(10),                                               
	"DAY_OF_WEEK_ABBREVIATION" VARCHAR2(3),                                        
	"JULIAN_DAY_NUM_OF_YEAR" NUMBER,                                          
	"JULIAN_DAY_NUM_ABSOLUTE" NUMBER,                                        
	"IS_WEEKDAY" CHAR(1),                                                          
	"IS_US_CIVIL_HOLIDAY" CHAR(1) DEFAULT 'N',                                     
	"IS_LAST_DAY_OF_WEEK" CHAR(1) DEFAULT 'N',                                     
	"IS_LAST_DAY_OF_MONTH" CHAR(1) DEFAULT 'N',                                    
	"IS_LAST_DAY_OF_QUARTER" CHAR(1) DEFAULT 'N',                                  
	"IS_LAST_DAY_OF_YEAR" CHAR(1) DEFAULT 'N',                                     
	"WEEK_NUM_OF_MONTH" NUMBER,                                               
	"WEEK_NUM_OF_QUARTER" NUMBER,                                             
	"MONTH_NAME" VARCHAR2(10),                                                     
	"MONTH_NAME_ABBREVIATION" VARCHAR2(3),                                         
	"QUARTER_NUM_OF_YEAR" NUMBER,                                             
	"YEAR_BEGIN_DATE_KEY" NUMBER,                                             
	"YEAR_END_DATE_KEY" NUMBER,                                               
	"YYYYMM" VARCHAR2(6),                                                          
	"YYYYMMDD" VARCHAR2(8),                                                        
	"DDMONYYYY" VARCHAR2(9),                                                       
	 CONSTRAINT "PK_DIM_DAY" PRIMARY KEY ("DATE_KEY")                              
   );
                                                                           
  CREATE TABLE APPLICATION.NAMED_FILTER
   (    "KEY" NUMBER NOT NULL,
        "NAME" VARCHAR2(25) NOT NULL,
        "MODEL" VARCHAR2(100) NOT NULL,
        "DEFINITION" VARCHAR2(4000) NOT NULL,
        "EMAIL_SUBJECT" VARCHAR2(100),
        "EMAIL_MESSAGE" VARCHAR2(4000),
         CONSTRAINT "NAMED_FILTER_PK" PRIMARY KEY ("KEY")
   );                                                                               
                                                                                
  CREATE TABLE DIMENSIONS.IDTRACKING
   (	"ID" VARCHAR2(12),                                                         
	"NAME" VARCHAR2(30),                                                           
	"CARTYPE" VARCHAR2(30)                                                         
   );
                                                                                
  CREATE TABLE APPLICATION.NAMED_FILTER_USERS
   (	"KEY" NUMBER,                                              
	"USERNAME" VARCHAR2(25) NOT NULL,                                       
	"PASSWORD" VARCHAR2(125) NOT NULL,                                      
	"EMAIL_LIST" VARCHAR2(4000),                                                   
	 CONSTRAINT "NAMED_FILTER_USERS_PK" PRIMARY KEY ("KEY")                        
   );
                                                                                
  CREATE TABLE APPLICATION.NAMED_FILTER_WATCHLIST
   (	"USER_KEY" NUMBER,                                         
	"FILTER_KEY" NUMBER,                                           
	"COLOR" NUMBER,                                                                
	"EMAIL" CHAR(1),                                                               
	 CONSTRAINT "NAMED_FILTER_WATCHLIST_PK" PRIMARY KEY ("USER_KEY", "FILTER_KEY") 
   );
                                                                                
  CREATE TABLE DIMENSIONS.AIRPORTS
   (	"CODE" VARCHAR2(3),                                                        
	"NAME" VARCHAR2(100),                                                          
	"CITY" VARCHAR2(100),                                                          
	"STATE" VARCHAR2(2),                                                           
	"COUNTRY" VARCHAR2(50),                                                        
	"LATITUDE" NUMBER,                                                             
	"LONGITUDE" NUMBER,                                                            
	 PRIMARY KEY ("CODE")                                                          
   );
                                                                                
  CREATE TABLE DIMENSIONS.UNIQUE_CARRIERS
   (	"CODE" VARCHAR2(8),                                                        
	"DESCRIPTION" VARCHAR2(100),                                                   
	 CONSTRAINT "UNIQUE_CARRIERS_PK" PRIMARY KEY ("CODE")                          
   );

  CREATE TABLE DIMENSIONS.ZIONTRACKING
   (	"ZIONTRACKINGKEY" NUMBER,                                                  
	"BANK" VARCHAR2(8),                                                            
	"ACCOUNT" VARCHAR2(16),                                                        
	"TRANSACTIONTYPE" VARCHAR2(8),                                                 
	"DATESTAMP" VARCHAR2(8),                                                       
	"BRANCHID" VARCHAR2(8)                                                         
   );
                                                                                
  CREATE TABLE APPLICATION.ALERTVIZ_GRAPHSETTINGS
   (	"PREFIX" VARCHAR2(400) NOT NULL,                                    
	"NAME" VARCHAR2(100),                                         
	"XML" CLOB
   );

  CREATE TABLE DIMENSIONS.DSHIELD_TOPIPS
   (	"IP" VARCHAR2(15),                                                         
	"DOMAIN" VARCHAR2(100),                                                        
	 PRIMARY KEY ("IP")                                                            
   );

  CREATE TABLE DIMENSIONS.DSHIELD_DAILYSOURCES
   (	"SOURCEIP" VARCHAR2(15),                                                   
	"TARGETPORT" NUMBER,                                                           
	"PROTOCOL" NUMBER,                                                             
	"REPORTS" NUMBER,                                                              
	"TARGETS" NUMBER,                                                              
	"FIRSTSEEN" DATE,                                                              
	"LASTSEEN" DATE                                                                
   );

  CREATE TABLE DIMENSIONS.LOCALNETWORKS
   (	"NETWORKNAME" VARCHAR2(30),                                                
	"IPSTART" VARCHAR2(16),                                                        
	"IPEND" VARCHAR2(16)                                                           
   );
