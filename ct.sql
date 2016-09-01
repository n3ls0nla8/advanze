set lin 500
set ver off
col OWNER format a10
col CONSTRAINT_NAME format a30 wrap
col TABLE_NAME format a30 wrap
col COLUMN_NAME format a30 wrap
col PARENT_TABLE format a30 wrap
undefine table_name
SELECT c.owner, a.table_name, a.column_name, a.constraint_name, '-->' ref, c.r_owner, c_pk.table_name r_table_name, (select accol.column_name from all_cons_columns accol where accol.constraint_name=c_pk.constraint_name) r_column 
FROM all_cons_columns a JOIN all_constraints c ON a.owner = c.owner AND a.constraint_name = c.constraint_name 
JOIN all_constraints c_pk ON c.r_owner = c_pk.owner AND c.r_constraint_name = c_pk.constraint_name 
WHERE c.constraint_type = 'R' AND c_pk.table_name = upper('&table_name');
