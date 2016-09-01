set lin 300
col INDEX_OWNER format a12 wrap
col TABLE_OWNER format a12 wrap
col COLUMN_NAME format a30 wrap
SELECT * FROM ALL_IND_COLUMNS WHERE TABLE_NAME LIKE upper('&table_name');
