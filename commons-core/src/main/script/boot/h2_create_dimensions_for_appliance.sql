-- Initialize and populate dimension tables for appliance use since the appliance currently does not contain Table Manager
-- Script needs to be idempotent

CREATE TABLE IF NOT EXISTS "DIMENSIONS"."SUSPICIOUS_IPS"
(
   IP varchar(2147483647) PRIMARY KEY NOT NULL,
   PORT integer NOT NULL,
   DESCRIPTION varchar(2147483647),
   SOURCE varchar(2147483647),
   TIMESTAMP timestamp
)
;
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_716 ON "DIMENSIONS"."SUSPICIOUS_IPS"(IP)
;

-- Load an initial set of data pending dynamic execution
MERGE INTO DIMENSIONS.SUSPICIOUS_IPS(IP, PORT, DESCRIPTION, SOURCE, TIMESTAMP) KEY(IP) VALUES
('43.255.191.224', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.691'),
('43.255.191.225', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.691'),
('43.255.191.226', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.691'),
('43.255.191.255', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.691'),
('195.154.44.124', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.691'),
('74.91.17.106', 0, 'Malware Domain', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.691'),
('37.228.93.44', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.691'),
('37.228.93.45', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.691'),
('37.228.93.46', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.691'),
('37.228.93.47', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.48', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.49', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.50', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.51', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.52', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.53', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.54', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.55', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.56', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.57', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.58', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.59', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.60', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.61', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.62', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.63', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.64', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.692'),
('37.228.93.65', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.67', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.68', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.69', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.70', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.71', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.72', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.73', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.74', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693');
MERGE INTO DIMENSIONS.SUSPICIOUS_IPS(IP, PORT, DESCRIPTION, SOURCE, TIMESTAMP) KEY(IP) VALUES
('37.228.93.75', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.76', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.77', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.78', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.79', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.80', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.81', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.82', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.83', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.84', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.693'),
('37.228.93.85', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.694'),
('37.228.93.86', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.694'),
('37.228.93.87', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.694'),
('37.228.93.88', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.694'),
('37.228.93.89', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.694'),
('37.228.93.90', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.694'),
('37.228.93.92', 0, 'Scanning Host', 'Alienvault IP Reputation Database', TIMESTAMP '2015-03-26 11:00:15.694');



CREATE TABLE IF NOT EXISTS "DIMENSIONS"."SUSPICIOUS_DOMAINS"
(
   DOMAIN varchar(2147483647) PRIMARY KEY NOT NULL,
   PORT integer NOT NULL,
   DESCRIPTION varchar(2147483647),
   SOURCE varchar(2147483647),
   TIMESTAMP timestamp
);

CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_6C ON "DIMENSIONS"."SUSPICIOUS_DOMAINS"(DOMAIN);




