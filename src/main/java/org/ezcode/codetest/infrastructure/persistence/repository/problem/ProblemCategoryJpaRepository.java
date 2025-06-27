package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.ProblemCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProblemCategoryJpaRepository extends JpaRepository<ProblemCategory, Long> {

	@EntityGraph(attributePaths = "category")
	List<ProblemCategory> findByProblemId(Long problemId);

	void deleteAllByProblemId(Long problemId);

	@EntityGraph(attributePaths = "category")
	List<ProblemCategory> findByProblemIdIn(List<Long> problemIds);
}
