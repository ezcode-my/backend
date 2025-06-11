package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProblemJpaRepository extends JpaRepository<Problem, Long> {

	Page<Problem> findByCategoryAndIsDeletedIsFalse(Category category, Pageable pageable);

	Page<Problem> findByIsDeletedIsFalse(Pageable pageable);

	@Query("SELECT p FROM Problem p WHERE p.isDeleted = false AND p.id = :problemId")
	Optional<Problem> findByIdNotDeleted(Long problemId);

	@Query("""
		select distinct p
		from Problem p
		left join fetch p.testcases
		where p.id = :problemId
		""")
	Optional<Problem> findProblemWithTestcasesById(@Param("problemId") Long problemId);

	boolean existsByTitleAndIsDeletedIsFalse(String title);
}
