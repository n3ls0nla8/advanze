package org.bitbucket.risu8.nuije.hibernate.validator.constraints.constraintvalidator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bitbucket.risu8.nuije.hibernate.validator.constraints.UniqueProperties;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UniquePropertiesValidator implements ConstraintValidator<UniqueProperties, Serializable> {

    @Autowired
    private SessionFactory sessionFactory;

    private UniqueConstraint[] uniqueConstraints;
    private Class<?> clazz;

    @Override
    public void initialize(UniqueProperties constraintAnnotation) {
        clazz = constraintAnnotation.value();
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            return;
        }
        uniqueConstraints = table.uniqueConstraints();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(Serializable value, ConstraintValidatorContext context) {
        if (uniqueConstraints == null || uniqueConstraints.length == 0) {
            return true;
        }
        try {
            for (UniqueConstraint uc : uniqueConstraints) {
                if (!checkUnique(value, Arrays.asList(uc.columnNames()))) {
                    return false;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    private boolean checkUnique(Serializable target, List<String> columnNames) throws IllegalAccessException {
        if (columnNames.isEmpty()) {
            return true;
        }
        Map<String, Object> eqs = new HashMap<>();
        for (String col : columnNames) {
            String c = StringUtils.removeEnd(col, "_id");
            eqs.put(c, FieldUtils.readDeclaredField(target, c));
        }
        return sessionFactory.getCurrentSession().createCriteria(clazz).add(Restrictions.allEq(eqs)).setProjection(Projections.rowCount()).uniqueResult().equals(0L);
    }
}
