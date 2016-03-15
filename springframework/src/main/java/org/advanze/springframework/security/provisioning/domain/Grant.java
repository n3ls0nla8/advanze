package org.bitbucket.risu8.nuije.springframework.security.provisioning.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "SEC_GRANT")
@Getter
@Setter
public class Grant implements Serializable, Comparable<Grant> {

    @Id
    private Long id;
    @ManyToOne
    private Role role;
    @ManyToOne
    private Permission permission;

    public Grant() {}

    public Grant(Role role, Permission permission) {
        this.role = role;
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grant that = (Grant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Grant o) {
        if (o == null) {
            return 1;
        }
        return id < o.getId() ? -1 : id > o.getId() ? 1 : 0;
    }
}
