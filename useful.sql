------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set lin 32767
col OWNER format a10 wrap
col DATA_TYPE format a10 wrap
col DATA_DEFAULT format a20 wrap
SELECT OWNER, TABLE_NAME, COLUMN_NAME, DATA_TYPE, DATA_LENGTH, DATA_PRECISION, DATA_SCALE, NULLABLE, DEFAULT_LENGTH, DATA_DEFAULT FROM ALL_TAB_COLS WHERE column_name LIKE upper('%&column_name');
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set lin 500
set ver off
col OWNER format a10 wrap
col CONSTRAINT_NAME format a10 wrap
col TABLE_NAME format a10 wrap
col SEARCH_CONDITION format a10 wrap
col R_OWNER format a10 wrap
col R_CONSTRAINT_NAME format a10 wrap
col INDEX_OWNER format a10 wrap
col INDEX_NAME format a10 wrap
col VALIDATED format a10 wrap
col GENERATED format a10 wrap
SELECT * FROM ALL_CONSTRAINTS A WHERE CONSTRAINT_NAME IN (SELECT C.CONSTRAINT_NAME FROM ALL_CONS_COLUMNS C WHERE TABLE_NAME LIKE UPPER('&parent_table_name%'));
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set lin 500
set ver off
col OWNER format a10
col CONSTRAINT_NAME format a30 wrap
col TABLE_NAME format a40 wrap
col COLUMN_NAME format a30 wrap
SELECT racc.* FROM ALL_CONS_COLUMNS racc WHERE (OWNER, CONSTRAINT_NAME, TABLE_NAME) IN (SELECT OWNER, CONSTRAINT_NAME, TABLE_NAME FROM ALL_CONSTRAINTS WHERE R_CONSTRAINT_NAME IN (SELECT CONSTRAINT_NAME FROM ALL_CONSTRAINTS WHERE CONSTRAINT_TYPE IN ('P','U') AND TABLE_NAME = UPPER('&parent_table_name')));
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set lin 200
set ver off
set echo off
set trims off
set pages 9999
col OSUSER format a10
col MACHINE format a10
col PROCESS format a8
col PROGRAM format a30
col SQL_TEXT format a50 wrap
SELECT s.OSUSER, s.MACHINE, s.PROCESS, s.PROGRAM, s.STATUS, s.SID, s.SERIAL#, a.ADDRESS, a.HASH_VALUE, a.DISK_READS, a.BUFFER_GETS, a.SQL_TEXT FROM V$SESSION S JOIN V$SQLAREA A ON A.HASH_VALUE = s.SQL_HASH_VALUE ORDER BY s.OSUSER, a.DISK_READS;
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set serveroutput on
set ver off
set echo off
SELECT * FROM V$SQLTEXT WHERE ADDRESS='&address' and HASH_VALUE='&hash_value' ORDER BY PIECE;
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set lin 500
col HOST_NAME format a10
SELECT * FROM V$INSTANCE ORDER BY HOST_NAME;
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set lin 500
set pages 9999
set ver off
col OWNER format a8
col NAME format a8
col TYPE format a8
col LINE format 999999
col TEXT format a120 wrap
SELECT * FROM ALL_SOURCE WHERE NAME LIKE UPPER('&source_name') ORDER BY LINE;
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
