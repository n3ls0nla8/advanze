package org.bitbucket.risu8.nuije.springframework.security.provisioning.dao.hibernate;

import lombok.Setter;
import org.bitbucket.risu8.nuije.springframework.security.provisioning.dao.UserDao;
import org.bitbucket.risu8.nuije.springframework.security.provisioning.domain.User;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.transaction.annotation.Transactional;

@Setter
public class UserDaoImpl implements UserDao {

    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return (User) sessionFactory.getCurrentSession().createCriteria(User.class).createAlias("userRoles", "ur", JoinType.LEFT_OUTER_JOIN).createAlias("role", "r", JoinType.LEFT_OUTER_JOIN).createAlias("grants", "g", JoinType.LEFT_OUTER_JOIN).createAlias("permission", "p", JoinType.LEFT_OUTER_JOIN).add(Restrictions.eq("username", username)).uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer countByUsername(String username) {
        return (Integer) sessionFactory.getCurrentSession().createCriteria(User.class).add(Restrictions.eq("username", username)).setProjection(Projections.rowCount()).uniqueResult();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveOrUpdate(User user) {
        sessionFactory.getCurrentSession().saveOrUpdate(user);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(User user) {
        sessionFactory.getCurrentSession().delete(user);
    }

}
