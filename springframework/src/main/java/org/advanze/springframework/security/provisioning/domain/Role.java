package org.advanze.springframework.security.provisioning.domain;

import lombok.Getter;
import lombok.Setter;
import org.bitbucket.risu8.nuije.hibernate.validator.constraints.UniqueProperties;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "SEC_ROLE", uniqueConstraints = @UniqueConstraint(columnNames = { "role" }))
@UniqueProperties(Role.class)
@Getter
@Setter
public class Role implements GrantedAuthority, Comparable<Role> {

    private static final long serialVersionUID = -7648262787508592475L;

    @Id
    private Long id;
    @Column
    private String role;
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<UserRole>();
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Grant> grants = new HashSet<Grant>();

    public Role() {}

    public Role(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        return Objects.equals(id, role1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.role;
    }

    @Override
    public int compareTo(Role o) {
        if (o == null) {
            return 1;
        }
        return id < o.getId() ? -1 : id > o.getId() ? 1 : 0;
    }
}
