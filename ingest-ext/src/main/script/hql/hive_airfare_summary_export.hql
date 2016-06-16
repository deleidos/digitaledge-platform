create table faresummary_export (origin string,
   dest string,
   year int,
   quarter int,
   avgpassengers double,
   avgmarketfare double, 
   avgmarketdistance double,
   origin_city string, 
   dest_city string)
   row format delimited fields terminated by ',' lines terminated by '\n'  STORED AS TEXTFILE LOCATION '/tmp/faresummary_export';
   
insert overwrite table faresummary_export select * from faresummary;