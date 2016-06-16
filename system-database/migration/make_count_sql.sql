select 'select '''||table_name||''', count(*) from '||table_schema||'.'||table_name||';' from INFORMATION_SCHEMA.tables where TABLE_SCHEMA in ('DIMENSIONS','APPLICATION') order by table_name;
