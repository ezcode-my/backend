package org.ezcode.codetest.domain.problem.repository;

import org.ezcode.codetest.application.problem.dto.request.ProblemSearchCondition;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProblemRepositoryCustom {
	Page<Problem> searchByCondition(Pageable pageable, ProblemSearchCondition searchCondition);
}
