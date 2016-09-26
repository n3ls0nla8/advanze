set lin 800
col GRANTOR format a20
col GRANTEE format a20
col TABLE_SCHEMA format a20
col PRIVILEGE format a20
col GRANTABLE format a10
col HIERARCHY format a10

SELECT * FROM ALL_TAB_PRIVS WHERE TABLE_NAME=upper('&table_name') ORDER BY GRANTEE;
