package org.ezcode.codetest.domain.problem.repository;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.Problem;

public interface ProblemSearchRepository {

	// 검색용 (검색 전용 DTO 또는 엔티티 리턴)
	List<Problem> searchProblems(String keyword);
}
