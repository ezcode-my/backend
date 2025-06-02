package org.ezcode.codetest.infrastructure.persitence.repository.problem;

import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProblemJpaRepository extends JpaRepository<Problem, Long> {

	Page<Problem> findByCategoryAndIsDeletedIsFalse(Category category, Pageable pageable);

	Page<Problem> findByIsDeletedIsFalse(Pageable pageable);

	@Query("SELECT p FROM Problem p WHERE p.isDeleted = false AND p.id = :problemId")
	Optional<Problem> findByIdNotDeleted(Long problemId);
}
