package org.bitbucket.risu8.nuije.springframework.jdbc.core;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Setter
@Slf4j
@Deprecated
public abstract class CompositeBeanPropertyRowMapper<T> implements RowMapper<T> {

    private List<BeanPropertyRowMapper> beanPropertyRowMappers = new ArrayList<BeanPropertyRowMapper>();

    public CompositeBeanPropertyRowMapper() {}

    public CompositeBeanPropertyRowMapper(Class ... mappedClasses) {
        for (Class c : mappedClasses) {
            BeanPropertyRowMapper<Class> m = new BeanPropertyRowMapper<Class>(c);
            beanPropertyRowMappers.add(m);
        }
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Object> objs = new ArrayList<Object>(beanPropertyRowMappers.size());
        for (BeanPropertyRowMapper mapper : beanPropertyRowMappers) {
            Object o = mapper.mapRow(rs, rowNum);
            objs.add(o);
            log.trace("Mapped {}", o.getClass());
        }
        return buildAssociations(objs);
    }

    protected abstract T buildAssociations(List<Object> mappedObjects);
}
