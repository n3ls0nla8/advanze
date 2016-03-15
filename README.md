# README #

Nuije aims to add more features to various Spring modules.

JIRA is hosted in sonatype: https://issues.sonatype.org/browse/OSSRH-16895

### Service method security ###

    <sec:global-method-security pre-post-annotations="enabled">
        <sec:expression-handler ref="methodSecurityExpressionHandler"/>
    </sec:global-method-security>

    <bean id="methodSecurityExpressionHandler" class="org.bitbucket.risu8.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler">
        <property name="permissionEvaluator">
            <bean class="org.bitbucket.risu8.springframework.security.access.DefaultPermissionEvaluator"/>
        </property>
    </bean>

    @PreAuthorize("hasPermission('CONFIG_VIEW')")
    public List<Config> search(Config config) {

### Spring Security credentials encode ###

    @Override
    @CredentialsChange
    @Transactional(rollbackFor = Throwable.class)
    public void createUser(org.bitbucket.risu8.springframework.security.provisioning.domain.User user) throws BindException {

### Unique record check ###

    @Entity
    @Table(name = "SEC_INTERCEPT_URL", uniqueConstraints = {@UniqueConstraint(columnNames = {"path", "method"})})
    @UniqueProperties(InterceptUrl.class)

### Dynamic access decision ###

    <sec:http use-expressions="true" authentication-manager-ref="authenticationManager">
        <sec:form-login login-page="/login.htm" authentication-success-handler-ref="authenticationSuccessHandler" authentication-failure-handler-ref="authenticationFailureHandler" authentication-details-source-ref="authenticationDetailsSource"/>
        <sec:logout/>
        <sec:custom-filter before="FILTER_SECURITY_INTERCEPTOR" ref="filterSecurityInterceptor"/>
    </sec:http>

    <bean id="filterSecurityInterceptor" class="org.springframework.security.web.access.intercept.FilterSecurityInterceptor" p:observeOncePerRequest="true" p:authenticationManager-ref="authenticationManager" p:accessDecisionManager-ref="accessDecisionManager" p:securityMetadataSource-ref="filterInvocationSecurityMetadataSource"/>

    <bean id="filterInvocationSecurityMetadataSource" class="org.bitbucket.risu8.springframework.security.web.access.expression.InterceptUrlFilterInvocationSecurityMetadataSource" p:interceptUrlService-ref="interceptUrlService" p:expressionHandler-ref="webSecurityExpressionHandler"/>

    <bean id="interceptUrlService" class="org.bitbucket.risu8.springframework.security.web.access.expression.service.impl.InterceptUrlServiceImpl" p:interceptUrlDao-ref="interceptUrlDao"/>

    <bean id="authenticationDetailsSource" class="org.bitbucket.risu8.springframework.security.web.authentication.InterceptUrlsWebAuthenticationDetailsSource" p:interceptUrlService-ref="interceptUrlService"/>

    <bean id="interceptUrlDao" class="org.bitbucket.risu8.springframework.security.web.access.expression.dao.hibernate.InterceptUrlDaoImpl" p:sessionFactory-ref="sessionFactory"/>

    <bean id="webSecurityExpressionHandler" class="org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler"/>

    <bean id="accessDecisionManager" class="org.springframework.security.access.vote.AffirmativeBased">
        <constructor-arg>
            <util:list>
                <bean class="org.bitbucket.risu8.springframework.security.web.access.expression.InterceptUrlVoter" p:expressionHandler-ref="webSecurityExpressionHandler"/>
                <bean class="org.springframework.security.web.access.expression.WebExpressionVoter" p:expressionHandler-ref="webSecurityExpressionHandler"/>
            </util:list>
        </constructor-arg>
    </bean>

    <bean id="authenticationSuccessHandler" class="org.bitbucket.risu8.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler" p:userDao-ref="userDao" p:requestRedirectMapping-ref="requestRedirectMapping"/>

    <bean id="authenticationFailureHandler" class="org.bitbucket.risu8.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler" p:redirectStrategy-ref="redirectStrategy" p:defaultFailureUrl="/login.htm?login_error=1">
        <property name="exceptionMappings">
            <util:map>
                <entry key="org.springframework.security.authentication.CredentialsExpiredException" value="/user/changePassword.htm"/>
            </util:map>
        </property>
    </bean>

    <bean id="redirectStrategy" class="org.bitbucket.risu8.springframework.security.web.SavedRequestParamRedirectStrategy" p:requestRedirectMapping-ref="requestRedirectMapping"/>

    <util:set id="requestRedirectMapping">
        <bean class="org.bitbucket.risu8.springframework.security.web.authentication.SessionRequestKeyPair" p:sessionKey="org.bitbucket.risu8.springframework.security.web.SavedRequestParamRedirectStrategy.username" p:requestKey="username"/>
    </util:set>
