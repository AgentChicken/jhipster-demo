package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Federation.
 */
@Entity
@Table(name = "federation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Federation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "syndicates")
    private String syndicates;

    @OneToOne
    @JoinColumn(unique = true)
    private Syndicate syndicate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Federation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSyndicates() {
        return syndicates;
    }

    public Federation syndicates(String syndicates) {
        this.syndicates = syndicates;
        return this;
    }

    public void setSyndicates(String syndicates) {
        this.syndicates = syndicates;
    }

    public Syndicate getSyndicate() {
        return syndicate;
    }

    public Federation syndicate(Syndicate syndicate) {
        this.syndicate = syndicate;
        return this;
    }

    public void setSyndicate(Syndicate syndicate) {
        this.syndicate = syndicate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Federation federation = (Federation) o;
        if (federation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, federation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Federation{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", syndicates='" + syndicates + "'" +
            '}';
    }
}
