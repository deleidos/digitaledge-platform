create table faresummary_raw (origin string,
   dest string,
   year int,
   quarter int,
   passengers double,
   marketfare double, 
   marketdistance double,
   origin_city string, 
   dest_city string);

insert overwrite table faresummary_raw
   select get_json_object(ritafaredatafull_raw.json, '$.origin'),
     get_json_object(ritafaredatafull_raw.json, '$.dest'),
     get_json_object(ritafaredatafull_raw.json, '$.year'),
     get_json_object(ritafaredatafull_raw.json, '$.quarter'),
     get_json_object(ritafaredatafull_raw.json, '$.passengers'),
     get_json_object(ritafaredatafull_raw.json, '$.marketFare'),
     get_json_object(ritafaredatafull_raw.json, '$.distanceGroup'),
     get_json_object(ritafaredatafull_raw.json, '$.D_origin.city_abr'),  
     get_json_object(ritafaredatafull_raw.json, '$.D_dest.city_abr') from ritafaredatafull_raw 
     group by ritafaredatafull_raw.json;
   
create table faresummary (origin string,
   dest string,
   year int,
   quarter int,
   avgpassengers double,
   avgmarketfare double, 
   avgmarketdistance double,
   origin_city string, 
   dest_city string);

insert overwrite table faresummary   
    select origin_city,dest_city,origin,dest,year,quarter,avg(passengers),avg(marketfare),avg(marketdistance)
      from faresummary_raw origin_city;

insert overwrite table faresummary
  select origin,dest,year,quarter,avg(passengers),avg(marketfare),avg(marketdistance),origin_city,dest_city
      from faresummary_raw group by origin,dest,year,quarter,origin_city,dest_city;
     
insert overwrite table faresummary
   select * from ritafaredatafull_raw 
      a lateral view json_tuple(json,'origin','dest','year','quarter',
      'passengers','marketFare','distanceGroup','D_origin_abr.city','D_dest_abr.city') b as 
        origin,dest,year,quarter,passengers,marketFare,distanceGroup,originCity,destCity
          group by origin,dest,year,quarter,passengers,marketFare,distanceGroup,originCity,destCity limit 5;

     
     
     
     
     
select * from ritafaredatafull_raw 
      a lateral view json_tuple(json,'origin','dest','year','quarter',
       'passengers','marketFare','distanceGroup','D_origin.city','D_dest.city') b as f1,f2,f3,f4,f5,f6,f7,f8,f9
      where f1 like '%LA%' group by f8,f9 limit 4;
      
select * from ritafaredatafull_raw 
      a lateral view json_tuple(json,'origin','dest','year','quarter',
       'passengers','marketFare','distanceGroup','D_origin.city','D_dest.city') b as f1,f2,f3,f4,avg(f5),avg(f6),avg(f7),upper(f8),upper(f9)
      group by upper(f8),upper(f9) limit 14;