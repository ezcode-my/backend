package org.ezcode.codetest.domain.problem.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.ezcode.codetest.domain.problem.model.ProblemSearchCondition;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProblemRepository {

	Problem save(Problem problem);

	Optional<Problem> findByIdNotDeleted(Long problemId);

	Set<String> findAutoComplete(String keyword);

	Page<Problem> searchByCondition(Pageable pageable, ProblemSearchCondition searchCondition);

	void delete(Problem problem);

	Optional<Problem> findProblemWithTestcasesById(Long problemId);

	boolean existsByTitleAndIsDeletedIsFalse(String title);

	void problemCountAdjustment(Long problemId, int correctInc);

    List<Long> getProblemIdList();
}
