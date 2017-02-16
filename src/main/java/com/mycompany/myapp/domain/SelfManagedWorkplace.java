package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SelfManagedWorkplace.
 */
@Entity
@Table(name = "self_managed_workplace")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SelfManagedWorkplace implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "employees")
    private Integer employees;

    @Column(name = "delegates")
    private String delegates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public SelfManagedWorkplace name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEmployees() {
        return employees;
    }

    public SelfManagedWorkplace employees(Integer employees) {
        this.employees = employees;
        return this;
    }

    public void setEmployees(Integer employees) {
        this.employees = employees;
    }

    public String getDelegates() {
        return delegates;
    }

    public SelfManagedWorkplace delegates(String delegates) {
        this.delegates = delegates;
        return this;
    }

    public void setDelegates(String delegates) {
        this.delegates = delegates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SelfManagedWorkplace selfManagedWorkplace = (SelfManagedWorkplace) o;
        if (selfManagedWorkplace.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, selfManagedWorkplace.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SelfManagedWorkplace{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", employees='" + employees + "'" +
            ", delegates='" + delegates + "'" +
            '}';
    }
}
