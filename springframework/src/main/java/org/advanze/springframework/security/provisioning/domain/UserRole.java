package org.bitbucket.risu8.nuije.springframework.security.provisioning.domain;

import lombok.Getter;
import lombok.Setter;
import org.bitbucket.risu8.nuije.hibernate.validator.constraints.UniqueProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "SEC_USER_ROLE", uniqueConstraints = @UniqueConstraint(columnNames = { "role_id", "user_id" }))
@UniqueProperties(UserRole.class)
@Getter
@Setter
public class UserRole implements Serializable, Comparable<UserRole> {

    private static final long serialVersionUID = 5829217895929358440L;

    @Id
    private Long id;
    @ManyToOne
    private Role role;
    @ManyToOne
    private User user;

    public UserRole() {}

    public UserRole(Role role, User user) {
        this.role = role;
        this.user = user;
    }

    @Override
    public int compareTo(UserRole o) {
        if (o == null) {
            return 1;
        }
        return id < o.getId() ? -1 : id > o.getId() ? 1 : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(id, userRole.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
