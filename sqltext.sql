set lin 200
set ver off
set echo off
col SQL_TEXT format a80 wrap
SELECT * FROM V$SQLTEXT WHERE ADDRESS='&address' and HASH_VALUE='&hash_value' ORDER BY PIECE;
