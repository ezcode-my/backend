package org.ezcode.codetest.domain.problem.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProblemRepository {

	Problem save(Problem problem);

	Optional<Problem> findById(Long problemId);

	Page<Problem> findByCategoryAndIsDeletedIsFalse(Category category, Pageable pageable);

	Page<Problem> findByIsDeletedIsFalse(Pageable pageable);

	Problem findByIdOrElseThrow(Long problemId);

	Problem delete(Problem problem);

}
