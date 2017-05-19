# Overview
Experiment the side effects of Oracle JDBC Driver and DB2 JDBC Driver when the Client and Server has different Timezones

# Environment Setup

## Oracle Server
`TODO`

## DB2 Server
`TODO`

## Sample Data

### Create Table

#### Oracle
`create table DUMMY (text varchar(255), created_timestamp number(10))`

#### DB2

`create table DUMMY (text varchar(255), created_timestamp int)`

### Insert Data

`insert into NCIM.DUMMY values ('This is from SQuirrel',1495091365);`

`insert into NCIM.DUMMY values ('This is from SQuirrel 2',1495098546);`

`insert into NCIM.DUMMY values ('This is from SQuirrel 3',1495099091);`

`insert into NCIM.DUMMY values ('This is from SQuirrel 4',1495100211);`

`insert into NCIM.DUMMY values ('This is from SQuirrel 5',1495102962);`

`insert into NCIM.DUMMY values ('This is from SQuirrel 6',1495103666);`

`insert into NCIM.DUMMY values ('This is from SQuirrel 6',1495103833);`

`insert into NCIM.DUMMY values ('This is from SQuirrel 7',1495105617);`

# Procedure 1

1. Execute `com.example.OracleTimestamps`

## Observation

|SQL Function + Literal|java.sql.Timestamp#toString|java.sql.Timestamp#getTimezoneOffset|java.sql.Timestamp#toGMTString|
|:--------------------:|:-------------------------:|:----------------------------------:|:----------------------------:|
|timestamp '1970-01-01 00:00:00'|Date/Time with Local Timezone|-480|Correct value|
|timestmap '1970-01-01 00:00:00 UTC'|Date/Time in UTC|-480|Correct value|
|timestamp '1970-01-01 00:00:00 America/New_York'|N/A|-480|N/A|
|CURRENT_TIMESTAMP|Date/Time with Local Timezone|-480|Correct value|

# Procedure 2

1. Execute `com.example.Db2Timestamps`

## Observation

|SQL Function + Literal|java.sql.Timestamp#toString|java.sql.Timestamp#getTimezoneOffset|java.sql.Timestamp#toGMTString|
|:--------------------:|:-------------------------:|:----------------------------------:|:----------------------------:|
|Timestamp('1970-01-01', '00:00:00')|Date/Time with local timezone|-480|Correct value|
|Timestamp('1970-01-01', '00:00:00') + current timezone|Date/Time with DB2 Server Application Timezone|-480|Not correct value|
|current_timestamp|Date/Time with DB2 Server Application Timezone|-480|Not correct value|
|current_timestamp - current timezone|Date/Time with UTC|-480|Not correct value|
|Timestamp('1970-01-01', '00:00:00') + 1491004800 seconds|Date/Time with unixtimestamp offset, not sure about Timezone|-480|N/A|
|Timestamp('1970-01-01', '00:00:00') + current timezone + 1491004800 seconds|Date/Time with DB2 Server Application Timezone|-480|Not correct value|

# Hypothesis 

1. Oracle Driver appears to be able to deal with Timezone conversion between some defined Timezone and UTC appropriately.
2. DB2 Driver appears to have some trouble computing the UTC

# Appendix

## SQL from Oracle v$sql table
`select * from (select TEXT, timestamp '1970-01-01 00:00:00' +  numToDSInterval(CREATED_TIMESTAMP, 'second') as CREATED_TIMESTAMP from NCIM.DUMMY) WHERE "CREATED_TIMESTAMP" > :1  AND "CREATED_TIMESTAMP" < CURRENT_TIMESTAMP ORDER BY "CREATED_TIMESTAMP" ASC`

## Check DB2 Server Application Timezone

1. From `db2diag.log` E.g. `/home/db2inst1/sqllib/db2dump/db2diag.log`
2. Take note of the log entries. E.g. `2017-05-19-13.31.24.701790+330 E23742691E465         LEVEL: Warning`
3. From `+330`, use `330/60` = `5.5`. This means a Timezone that is `+5.5` hours
4.

## Check Oracle Server Application Timezone

1. From https://docs.oracle.com/cd/B19306_01/server.102/b14200/functions039.htm
2. Use `SELECT DBTIMEZONE FROM DUAL`