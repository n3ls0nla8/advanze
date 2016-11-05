package org.advanze.springframework.security.web.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@Setter
@Getter
public class InterceptUrlsWebAuthenticationDetails extends WebAuthenticationDetails {

    private Map<RequestMatcher, Collection<ConfigAttribute>> interceptUrls;

    public InterceptUrlsWebAuthenticationDetails(HttpServletRequest request, Map<RequestMatcher, Collection<ConfigAttribute>> interceptUrls) {
        super(request);
        this.interceptUrls = interceptUrls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InterceptUrlsWebAuthenticationDetails that = (InterceptUrlsWebAuthenticationDetails) o;
        return super.equals(o) ? Objects.equals(interceptUrls, that.interceptUrls) : false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), interceptUrls);
    }

    @Override
    public String toString() {
        return super.toString() + "; InterceptUrls=" + interceptUrls;
    }
}
