package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Syndicate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Syndicate entity.
 */
@SuppressWarnings("unused")
public interface SyndicateRepository extends JpaRepository<Syndicate,Long> {

}
