package org.advanze.springframework.security.provisioning.domain;

import lombok.Getter;
import lombok.Setter;
import org.bitbucket.risu8.nuije.hibernate.validator.constraints.UniqueProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "SEC_USER", uniqueConstraints = @UniqueConstraint(columnNames = { "username" }))
@UniqueProperties(User.class)
@Getter
@Setter
public class User implements Serializable, Comparable<User> {

    private static final long serialVersionUID = 5642457774229996291L;

    @Id
    private Long id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private int loginAttempt;
    @Column
    private int enabled;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column
    private String createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginSuccessDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordChangeDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    @Column
    private String lastModifiedBy;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<UserRole>();

    @Override
    public int compareTo(User o) {
        if (o == null) {
            return 1;
        }
        return id < o.getId() ? -1 : id > o.getId() ? 1 : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
