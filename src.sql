set lin 500
set pages 9999
set ver off
col OWNER format a8
col NAME format a8
col TYPE format a8
col LINE format 999999
col TEXT format a120 wrap
SELECT * FROM ALL_SOURCE WHERE NAME LIKE UPPER('&source_name') AND TEXT LIKE '&code' ORDER BY LINE;
