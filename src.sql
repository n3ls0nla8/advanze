set lin 5000
set pages 9999
set trims on
set ver off
set echo off
SELECT TEXT FROM ALL_SOURCE WHERE NAME LIKE UPPER('&source_name') AND TEXT LIKE NVL('&code', '%') ORDER BY LINE;
