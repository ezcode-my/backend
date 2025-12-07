package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.repository.ProblemSearchRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProblemSearchRepositoryImpl implements ProblemSearchRepository {

	private final ProblemSearchJpaRepository searchJpaRepository;

	@Override
	public List<Problem> searchProblems(String keyword) {
		return searchJpaRepository.searchByKeyword(keyword);
	}
}
