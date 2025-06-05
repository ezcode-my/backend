package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestcaseJpaRepository extends JpaRepository<Testcase, Long> {

	List<Testcase> findAllByProblem(Problem problem);

}
