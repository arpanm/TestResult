package com.reliance.jpl.repository;

import com.reliance.jpl.domain.Build;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Build entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BuildRepository extends JpaRepository<Build, Long> {}
