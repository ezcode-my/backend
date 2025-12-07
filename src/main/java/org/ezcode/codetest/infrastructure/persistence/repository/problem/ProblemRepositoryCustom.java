package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.Set;

import org.ezcode.codetest.domain.problem.model.ProblemSearchCondition;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProblemRepositoryCustom {
	Set<String> findAutoComplete(String keyword);
	Page<Problem> searchByCondition(Pageable pageable, ProblemSearchCondition searchCondition);
}
