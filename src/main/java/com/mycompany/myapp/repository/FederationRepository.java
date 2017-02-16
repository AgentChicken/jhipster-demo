package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Federation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Federation entity.
 */
@SuppressWarnings("unused")
public interface FederationRepository extends JpaRepository<Federation,Long> {

}
