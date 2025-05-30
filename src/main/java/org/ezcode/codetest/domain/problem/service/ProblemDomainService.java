package org.ezcode.codetest.domain.problem.service;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.repository.ProblemRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemDomainService {

	private final ProblemRepository problemRepository;

	public Problem createProblem(Problem problem) {
		return problemRepository.save(problem);
	}



}
