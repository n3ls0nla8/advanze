package org.bitbucket.risu8.nuije.springframework.context.i18n;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.faces.context.FacesContext;
import java.util.Locale;

@Slf4j
@Aspect
public class JsfLocaleAspect {

    @Before("execution(* *..service..*(..))")
    public void activate() {
        FacesContext instance = FacesContext.getCurrentInstance();
        if (instance == null) {
            return;
        }
        Locale locale = instance.getViewRoot().getLocale();
        LocaleContextHolder.setLocale(locale);
        log.debug("Assigned JSF locale {} to current thread", locale);
    }
}
