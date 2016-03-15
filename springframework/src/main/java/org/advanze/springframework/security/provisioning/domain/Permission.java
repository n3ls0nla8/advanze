package org.bitbucket.risu8.nuije.springframework.security.provisioning.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "SEC_PERMISSION")
public class Permission implements Serializable, Comparable<Permission> {

    private static final long serialVersionUID = 9220325677486506855L;

    @Id
    private Long id;
    @Column
    private String name;
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Grant> grants = new HashSet<Grant>();

    @Override
    public int compareTo(Permission o) {
        if (o == null) {
            return 1;
        }
        return id < o.getId() ? -1 : id > o.getId() ? 1 : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
