package org.bitbucket.risu8.nuije.hibernate.validator.constraints.constraintvalidator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bitbucket.risu8.nuije.hibernate.validator.constraints.UniqueColumns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UniqueColumnsValidator implements ConstraintValidator<UniqueColumns, Serializable> {

    private NamedParameterJdbcTemplate jdbcTemplate;
    private String[] columnNames;
    private String tableName;
    private String[] propertyNames;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void initialize(UniqueColumns constraintAnnotation) {
        columnNames = constraintAnnotation.columnNames();
        propertyNames = constraintAnnotation.propertyNames();
        tableName = StringUtils.trimToEmpty(constraintAnnotation.tableName());
        Assert.isTrue(columnNames.length == propertyNames.length, "Columns must align with properties");
        Assert.hasText(tableName, "Table name must not be empty");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(Serializable target, ConstraintValidatorContext context) {
        try {
            List<Object> values = new ArrayList<Object>();
            for (int i = 0; i < propertyNames.length; i++) {
                String propertyName = StringUtils.trimToEmpty(propertyNames[i]);
                PropertyDescriptor desc = new PropertyDescriptor(propertyName, target.getClass());
                Method readMethod = desc.getReadMethod();
                values.add(readMethod.invoke(target));
            }
            Map<String, Object> params = new HashMap<String, Object>(values.size());
            for (int i = 0; i < columnNames.length; i++) {
                String c = StringUtils.trimToEmpty(columnNames[i]);
                params.put(c, values.get(i));
            }
            String sql = "select count(*) from " + tableName + (params.isEmpty() ? "" : " where ");
            for (String s : params.keySet()) {
                sql += s + "=:" + s + " and ";
            }
            sql = StringUtils.substringBeforeLast(sql, " and ");
            return jdbcTemplate.queryForObject(sql, params, Integer.class) == 0;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return false;
    }
}
