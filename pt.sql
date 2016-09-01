set lin 1000
set ver off
col ref format a5
col TABLE_NAME format a30 wrap
col COLUMN_NAME format a30 wrap
col R_TABLE_NAME format a30 wrap
col OWNER format a10 wrap
col R_OWNER format a10 wrap
col R_PK format a10 wrap
col R_COLUMN format a30 wrap
undefine table_name
SELECT c.owner, a.table_name, a.column_name, a.constraint_name, '-->' ref, c.r_owner, c_pk.table_name r_table_name, (select accol.column_name from all_cons_columns accol where accol.constraint_name=c_pk.constraint_name) r_column 
FROM all_cons_columns a JOIN all_constraints c ON a.owner = c.owner AND a.constraint_name = c.constraint_name 
JOIN all_constraints c_pk ON c.r_owner = c_pk.owner AND c.r_constraint_name = c_pk.constraint_name 
WHERE c.constraint_type = 'R' AND a.table_name = upper('&table_name');
