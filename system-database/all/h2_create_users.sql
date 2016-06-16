create user if not exists INGEST PASSWORD 'redacted';
create user if not exists APPUSER PASSWORD 'redacted';
alter user SA set password 'redacted';
ALTER USER INGEST ADMIN TRUE;
ALTER USER APPUSER ADMIN TRUE;