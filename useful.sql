------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set lin 32767
set ver off
col OWNER format a20 wrap
col DATA_TYPE format a10 wrap
col DATA_DEFAULT format a20 wrap
undefine table_name
SELECT OWNER, TABLE_NAME, COLUMN_NAME, DATA_TYPE, DATA_LENGTH, DATA_PRECISION, DATA_SCALE, NULLABLE, DEFAULT_LENGTH, DATA_DEFAULT FROM ALL_TAB_COLS WHERE TABLE_NAME LIKE UPPER('&table_name');
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
undefine table_name
SELECT * FROM ALL_CONSTRAINTS A WHERE CONSTRAINT_NAME IN (SELECT c.CONSTRAINT_NAME FROM ALL_CONS_COLUMNS c WHERE TABLE_NAME LIKE UPPER('&table_name'));
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set lin 500
set ver off
col OWNER format a10
col CONSTRAINT_NAME format a30 wrap
col TABLE_NAME format a40 wrap
col COLUMN_NAME format a30 wrap
col PARENT format a30 wrap
undefine parent_table_name
SELECT UPPER('&&parent_table_name') PARENT, racc.* FROM ALL_CONS_COLUMNS racc WHERE (OWNER, CONSTRAINT_NAME, TABLE_NAME) IN (SELECT OWNER, CONSTRAINT_NAME, TABLE_NAME FROM ALL_CONSTRAINTS WHERE R_CONSTRAINT_NAME IN (SELECT CONSTRAINT_NAME FROM ALL_CONSTRAINTS WHERE CONSTRAINT_TYPE IN ('P','U') AND TABLE_NAME = UPPER('&&parent_table_name')));
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set lin 1000
set ver off
col TABLE_NAME format a30 wrap
col COLUMN_NAME format a30 wrap
col R_TABLE_NAME format a30 wrap
col R_OWNER format a10 wrap
undefine child_table_name
SELECT a.table_name, a.column_name, a.constraint_name, c.owner, c.r_owner, c_pk.table_name r_table_name, c_pk.constraint_name r_pk FROM all_cons_columns a JOIN all_constraints c ON a.owner = c.owner AND a.constraint_name = c.constraint_name JOIN all_constraints c_pk ON c.r_owner = c_pk.owner AND c.r_constraint_name = c_pk.constraint_name WHERE c.constraint_type = 'R' AND a.table_name = upper('&child_table_name');
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set lin 200
set ver off
set echo off
set trims off
set pages 9999
col OSUSER format a10
col MACHINE format a10
col PROGRAM format a15 wrap
col PROCESS format a8
col DISK_READS format 999,999
col BUFFER_GETS format 9,999,999,999
col SORTS format 99,999
col SID format 999
col SQL_TEXT format a60 wrap
SELECT s.OSUSER, s.MACHINE, s.PROCESS, s.PROGRAM, s.STATUS, s.SID, s.SERIAL#, a.ADDRESS, a.HASH_VALUE, a.DISK_READS, a.BUFFER_GETS, a.SORTS, a.SQL_TEXT FROM V$SESSION S JOIN V$SQLAREA a ON a.HASH_VALUE = s.SQL_HASH_VALUE ORDER BY s.OSUSER, a.DISK_READS;
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
SELECT OWNER, OBJECT_NAME, STATISTIC_NAME, VALUE
FROM V$SEGMENT_STATISTICS
WHERE OBJECT_NAME IN
('APP_INTEREST_USER_CCY_CODES', 'FIXED_DEPOSIT_ACCOUNTS', 'CURRENT_ACCOUNT_BALANCES', 'CURRENT_ACCOUNTS', 'IRD_ACCOUNT_BALANCES', 
'APP_INTEREST_USER_CCY_CODES', 'IRD_ACCOUNTS', 'SAVINGS_ACCOUNT_BALANCES', 'APP_INTEREST_USER_CCY_CODES', 'FIXED_DEPOSIT_ACCOUNTS',
'APP_INTEREST_USER_CCY_CODES', 'SIGNATURE_CARDS', 'SECURITIES_ACCOUNTS', 'SECURITIES_ACCOUNT_BALANCES', 'SECURITIES',
'LOAN_PAYMENTS', 'APP_SYSTEM_PARAMETERS', 'LOAN_ACCOUNTS', 'LOAN_PAYMENTS', 'APP_SYSTEM_PARAMETERS', 'LOAN_ACCOUNT_BALANCES',
'CREDIT_CARD_ACCOUNTS', 'LG_ISSUES', 'UNIT_TRUST_FUNDS', 'UNIT_TRUST_ACCOUNT_BALANCES')
ORDER BY VALUE
/
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set lin 200
set ver off
set echo off
col SQL_TEXT format a80 wrap
SELECT * FROM V$SQLTEXT WHERE ADDRESS='&address' and HASH_VALUE='&hash_value' ORDER BY PIECE;
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set lin 200
col USER_NAME format a10
SELECT c.* FROM V$OPEN_CURSOR c JOIN V$SESSION s ON c.SID=s.SID AND c.USER_NAME='&username' AND s.TERMINAL='&tty';
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set verify off
set lines 200
set pagesize 999
col username format a13
col prog format a22
col sql_text format a41 wrap
col sid format 999
col child_number format 99999 heading CHILD
col ocategory format a10
col executions format 9,999,999
col executions_per_sec format 999,999.99
col etime format 9,999,999.99
col avg_etime format 9,999,999.99
col cpu format 9,999,999
col avg_cpu  format 9,999,999.99
col pio format 9,999,999
col avg_pio format 9,999,999.99
col lio format 9,999,999
col avg_lio format 9,999,999.99
col avg_fetches format 9,999,999.99
col avg_rows format 9,999,999
SELECT ADDRESS, HASH_VALUE, CHILD_NUMBER, EXECUTIONS,
EXECUTIONS/((SYSDATE-TO_DATE(FIRST_LOAD_TIME,'YYYY-MM-DD/HH24:MI:SS'))*(24*60*60)) EXECUTIONS_PER_SEC,
-- ELAPSED_TIME/1000000 ETIME,
(ELAPSED_TIME/1000000)/DECODE(NVL(EXECUTIONS,0),0,1,EXECUTIONS) AVG_ETIME,
-- CPU_TIME/1000000 CPU,
(CPU_TIME/1000000)/DECODE(NVL(EXECUTIONS,0),0,1,EXECUTIONS) AVG_CPU,
-- DISK_READS PIO,
DISK_READS/DECODE(NVL(EXECUTIONS,0),0,1,EXECUTIONS) AVG_PIO,
-- BUFFER_GETS LIO,
BUFFER_GETS/DECODE(NVL(EXECUTIONS,0),0,1,EXECUTIONS) AVG_LIO,
FETCHES/DECODE(NVL(EXECUTIONS,0),0,1,EXECUTIONS) AVG_FETCHES,
ROWS_PROCESSED/DECODE(NVL(EXECUTIONS,0),0,1,EXECUTIONS) AVG_ROWS,
SQL_TEXT
FROM V$SQL S
WHERE ADDRESS LIKE NVL('&ADDRESS',ADDRESS) AND HASH_VALUE LIKE NVL('&HASH_VALUE',HASH_VALUE);
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set lin 500
col HOST_NAME format a10
SELECT * FROM V$INSTANCE ORDER BY HOST_NAME;
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set lin 32767
set ver off
set feed off
col OWNER format a10
col TABLE_NAME format a30
col TABLESPACE_NAME format a15
col CLUSTER_NAME format a15
col IOT_NAME format a8
col BACKED_UP format a10
clear columns
define tn='CHC_COMMODITIES_ACCOUNTS'
SELECT OWNER, TABLE_NAME, TABLESPACE_NAME, CLUSTER_NAME, IOT_NAME, PCT_FREE, PCT_USED, INI_TRANS, MAX_TRANS, INITIAL_EXTENT, NEXT_EXTENT, MIN_EXTENTS, MAX_EXTENTS, PCT_INCREASE FROM ALL_TABLES WHERE TABLE_NAME LIKE UPPER('&tn');
SELECT FREELISTS, FREELIST_GROUPS, LOGGING, BACKED_UP, NUM_ROWS, BLOCKS, EMPTY_BLOCKS, AVG_SPACE, CHAIN_CNT, AVG_ROW_LEN, AVG_SPACE_FREELIST_BLOCKS, NUM_FREELIST_BLOCKS, DEGREE FROM ALL_TABLES WHERE TABLE_NAME LIKE UPPER('&tn');
SELECT INSTANCES, CACHE, TABLE_LOCK, SAMPLE_SIZE, LAST_ANALYZED, PARTITIONED, IOT_TYPE, TEMPORARY, SECONDARY, NESTED, BUFFER_POOL, ROW_MOVEMENT, GLOBAL_STATS, USER_STATS, DURATION, SKIP_CORRUPT, MONITORING, CLUSTER_OWNER, DEPENDENCIES, COMPRESSION FROM ALL_TABLES WHERE TABLE_NAME LIKE UPPER('&tn');
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
