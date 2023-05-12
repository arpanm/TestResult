package com.reliance.jpl.repository;

import com.reliance.jpl.domain.TestResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {}
