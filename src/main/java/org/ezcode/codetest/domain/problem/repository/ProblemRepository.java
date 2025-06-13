package org.ezcode.codetest.domain.problem.repository;

import java.util.Optional;

import org.ezcode.codetest.application.problem.dto.request.ProblemSearchCondition;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProblemRepository {

	Problem save(Problem problem);

	Optional<Problem> findByIdNotDeleted(Long problemId);

	Page<Problem> searchByCondition(Pageable pageable, ProblemSearchCondition searchCondition);

	void delete(Problem problem);

	Optional<Problem> findProblemWithTestcasesById(Long problemId);

	boolean existsByTitleAndIsDeletedIsFalse(String title);
}
