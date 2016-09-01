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
