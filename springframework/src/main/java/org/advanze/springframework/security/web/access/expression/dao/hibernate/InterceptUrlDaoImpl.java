package org.advanze.springframework.security.web.access.expression.dao.hibernate;

import lombok.Setter;
import org.advanze.springframework.security.web.access.expression.dao.InterceptUrlDao;
import org.advanze.springframework.security.web.access.expression.domain.InterceptUrl;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Setter
public class InterceptUrlDaoImpl implements InterceptUrlDao {

    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<InterceptUrl> findAll() {
        return sessionFactory.getCurrentSession().createCriteria(InterceptUrl.class).list();
    }
}
