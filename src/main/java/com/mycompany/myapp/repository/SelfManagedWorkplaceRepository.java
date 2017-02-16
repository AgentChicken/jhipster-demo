package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SelfManagedWorkplace;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SelfManagedWorkplace entity.
 */
@SuppressWarnings("unused")
public interface SelfManagedWorkplaceRepository extends JpaRepository<SelfManagedWorkplace,Long> {

}
