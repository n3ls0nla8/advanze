package org.advanze.springframework.security.web.access.expression.domain;

import lombok.Getter;
import lombok.Setter;
import org.advanze.springframework.security.provisioning.bean.ValidationHint;
import org.apache.commons.lang3.StringUtils;
import org.bitbucket.risu8.nuije.hibernate.validator.constraints.UniqueProperties;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "SEC_INTERCEPT_URL", uniqueConstraints = {@UniqueConstraint(columnNames = {"path", "method"})})
@UniqueProperties(InterceptUrl.class)
public class InterceptUrl implements Serializable, Comparable<InterceptUrl> {

    private static final long serialVersionUID = 8137795483634523974L;

    @Id
    private Long id;
    @Column
    @NotBlank(groups = {ValidationHint.Create.class, ValidationHint.Update.class})
    @Length(min = 1, max = 200, groups = {ValidationHint.Create.class, ValidationHint.Update.class})
    private String path;
    @Column
    @Length(min = 0, max = 20, groups = {ValidationHint.Create.class, ValidationHint.Update.class})
    private String method;
    @Column
    @NotBlank(groups = {ValidationHint.Create.class, ValidationHint.Update.class})
    @Length(min = 1, max = 60, groups = {ValidationHint.Create.class, ValidationHint.Update.class})
    private String access;

    @Override
    public int compareTo(InterceptUrl o) {
        int len = StringUtils.length(path);
        int olen = StringUtils.length(o.getPath());
        return len > olen ? -1 : len < olen ? 1 : 0;
    }

    @Override
    public String toString() {
        return "InterceptUrl{id=" + id + ", path='" + path + '\'' + ", method='" + method + '\'' + ", access='" + access + '\'' + '}';
    }
}
