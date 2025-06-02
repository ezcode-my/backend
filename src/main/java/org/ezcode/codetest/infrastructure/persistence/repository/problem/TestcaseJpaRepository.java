package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import org.ezcode.codetest.domain.problem.model.entity.Testcase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestcaseJpaRepository extends JpaRepository<Testcase, Long> {
}
