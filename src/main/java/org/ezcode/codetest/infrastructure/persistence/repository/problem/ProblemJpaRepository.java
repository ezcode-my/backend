package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProblemJpaRepository extends JpaRepository<Problem, Long> {

	@Query("SELECT p FROM Problem p WHERE p.isDeleted = false AND p.id = :problemId")
	Optional<Problem> findByIdNotDeleted(Long problemId);

	@Query("""
		select distinct p
		from Problem p
		left join fetch p.testcases
		left join fetch p.categories c
		left join fetch c.category
		where p.id = :problemId
		""")
	Optional<Problem> findProblemWithTestcasesById(@Param("problemId") Long problemId);

	boolean existsByTitleAndIsDeletedIsFalse(String title);
}
