set lin 500
col HOST_NAME format a10
SELECT * FROM V$INSTANCE ORDER BY HOST_NAME;
