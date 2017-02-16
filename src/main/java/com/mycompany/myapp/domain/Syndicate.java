package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Syndicate.
 */
@Entity
@Table(name = "syndicate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Syndicate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "workplaces")
    private String workplaces;

    @Column(name = "delegates")
    private String delegates;

    @OneToOne
    @JoinColumn(unique = true)
    private SelfManagedWorkplace workplace;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Syndicate name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkplaces() {
        return workplaces;
    }

    public Syndicate workplaces(String workplaces) {
        this.workplaces = workplaces;
        return this;
    }

    public void setWorkplaces(String workplaces) {
        this.workplaces = workplaces;
    }

    public String getDelegates() {
        return delegates;
    }

    public Syndicate delegates(String delegates) {
        this.delegates = delegates;
        return this;
    }

    public void setDelegates(String delegates) {
        this.delegates = delegates;
    }

    public SelfManagedWorkplace getWorkplace() {
        return workplace;
    }

    public Syndicate workplace(SelfManagedWorkplace selfManagedWorkplace) {
        this.workplace = selfManagedWorkplace;
        return this;
    }

    public void setWorkplace(SelfManagedWorkplace selfManagedWorkplace) {
        this.workplace = selfManagedWorkplace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Syndicate syndicate = (Syndicate) o;
        if (syndicate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, syndicate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Syndicate{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", workplaces='" + workplaces + "'" +
            ", delegates='" + delegates + "'" +
            '}';
    }
}
