package org.ezcode.codetest.domain.problem.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProblemRepository {

	Problem save(Problem problem);

	Optional<Problem> findById(Long id);

	Page<Problem> findAllByCategory(Category category, Pageable pageable);

	Problem findByIdOrElseThrow(Long id);

	Problem delete(Problem problem);

}
