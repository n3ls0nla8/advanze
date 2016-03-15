package org.bitbucket.risu8.nuije.springframework.security.provisioning.dao.hibernate;

import lombok.Setter;
import org.bitbucket.risu8.nuije.springframework.security.provisioning.dao.RoleDao;
import org.bitbucket.risu8.nuije.springframework.security.provisioning.domain.Role;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Setter
public class RoleDaoImpl implements RoleDao {

    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public Role findByRole(String role) {
        return (Role) sessionFactory.getCurrentSession().createCriteria(Role.class).add(Restrictions.eq("role", role)).uniqueResult();
    }
}
