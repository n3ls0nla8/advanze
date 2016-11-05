package org.advanze.springframework.security.provisioning;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.advanze.springframework.security.provisioning.bean.ValidationHint;
import org.advanze.springframework.security.provisioning.dao.RoleDao;
import org.advanze.springframework.security.provisioning.dao.UserDao;
import org.advanze.springframework.security.provisioning.domain.Role;
import org.advanze.springframework.security.provisioning.domain.User;
import org.advanze.springframework.security.provisioning.domain.UserRole;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class DefaultUserManager implements UserManager, ApplicationContextAware {

    @Value("#{appProp['security.user.firstLoginChangePassword'] ?: false}")
    private boolean firstLoginChangePassword;
    @Value("#{appProp['security.user.password.validDays'] ?: 0}")
    private int passwordValidDays;
    @Value("#{appProp['security.user.maxLoginAttempt'] ?: 0}")
    private int maxLoginAttempt;
    @Value("#{appProp['security.enableAuthorities'] ?: true}")
    private boolean enableAuthorities;

    @Setter
    private SmartValidator validator;
    @Setter
    private UserDao userDao;
    @Setter
    private RoleDao roleDao;
    @Setter
    private PasswordEncoder passwordEncoder;

    private ApplicationContext applicationContext;

    protected final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createUser(User user) throws BindException {
        Assert.notNull(user, "user must not be null");
        BindingResult errors = new BeanPropertyBindingResult(user, User.class.getName());
        validator.validate(user, errors, ValidationHint.Create.class);
        if (errors.hasErrors()) {
            throw new BindException(errors);
        }
        userDao.saveOrUpdate(user);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateUser(User user) throws BindException {
        Assert.notNull(user, "user must not be null");
        user.setLastModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        user.setLastModifiedDate(new Date());
        BindingResult errors = new BeanPropertyBindingResult(user, User.class.getName());
        validator.validate(user, errors, ValidationHint.Update.class);
        if (errors.hasErrors()) {
            throw new BindException(errors);
        }
        userDao.saveOrUpdate(user);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteUser(String username) throws BindException {
        Assert.isInstanceOf(String.class, username, "Username " + username + " must be String type");
        User u = userDao.findByUsername(username);
        if (u == null) {
            BindingResult errors = new MapBindingResult(new HashMap<String, Object>(), User.class.getName());
            errors.rejectValue("username", "org.advanze.springframework.security.provisioning.domain.User.username.notExists", new Object[]{username}, "User " + username + " not exists");
            throw new BindException(errors);
        }
        u.getUserRoles().clear();
        userDao.delete(u);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
        }

        String username = currentUser.getName();
        AuthenticationManager am = applicationContext.getBean("authenticationManager", AuthenticationManager.class);
        if (am != null) {
            log.debug("Reauthenticating user '{}' for password change request.", username);
            am.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
        }
        else {
            log.debug("No authentication manager set. Password won't be re-checked.");
        }

        doChangePassword(username, oldPassword, newPassword);

        SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(currentUser, newPassword));
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void changePasswordNoAuthentication(String username, String oldPassword, String newPassword) {
        doChangePassword(username, oldPassword, newPassword);
    }

    private void doChangePassword(String username, String oldPassword, String newPassword) {
        Date now = new Date();
        log.debug("Changing password for user '{}' at {}", username, now);

        String encPass = passwordEncoder.encodePassword(newPassword, username);
        User user = userDao.findByUsername(username);
        user.setPassword(encPass);
        user.setLastPasswordChangeDate(now);
        user.setLastModifiedBy(username);
        user.setLastModifiedDate(now);
        userDao.saveOrUpdate(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(String username) {
        return userDao.countByUsername(username) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            log.debug("Query returned no results for user '{}'", username);
            throw new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.notFound", new Object[] { username }, "Username {0} not found"));
        }

        Date lastLoginSuccessDate = user.getLastLoginSuccessDate();
        Date expiryDate = user.getExpiryDate();
        Date effectiveDate = user.getEffectiveDate();
        log.debug("Last login success date of user {} is {}", username, lastLoginSuccessDate);
        DateTime now = DateTime.now();
        Integer pwdLifeSpan = 0;
        if (lastLoginSuccessDate != null) {
            DateTime lastPasswordChangeDate = new DateTime(user.getLastPasswordChangeDate());
            log.debug("Last password modification date of user {} is {}. Time now is {}.", new Object[]{username, lastPasswordChangeDate, now});
            pwdLifeSpan = Days.daysBetween(lastPasswordChangeDate, now).getDays();
        }

        Set<Role> roles = new HashSet<Role>();
        for (UserRole ur : user.getUserRoles()) {
            roles.add(ur.getRole());
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), (user.getEnabled() == 1 && (expiryDate == null || now.isBefore(new DateTime(expiryDate))) && (effectiveDate == null || now.isAfter(new DateTime(effectiveDate)))), true, ((!firstLoginChangePassword || lastLoginSuccessDate != null) && (passwordValidDays <= 0 || pwdLifeSpan <= passwordValidDays)), (maxLoginAttempt <= 0 ? true : user.getLoginAttempt() < maxLoginAttempt), roles);
    }

    protected Authentication createNewAuthentication(Authentication currentAuth, String newPassword) {
        UserDetails user = loadUserByUsername(currentAuth.getName());

        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(user, newPassword, user.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());

        return newAuthentication;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
