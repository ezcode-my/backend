package org.ezcode.codetest.domain.problem.service;

import org.ezcode.codetest.domain.problem.exception.ProblemException;
import org.ezcode.codetest.domain.problem.exception.code.ProblemExceptionCode;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.repository.ProblemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemDomainService {

	private final ProblemRepository problemRepository;

	public Problem createProblem(Problem problem) {
		return problemRepository.save(problem);
	}

	public Page<Problem> getProblemsByCategoryList(Category category, Pageable pageable) {

		return problemRepository.findByCategoryAndIsDeletedIsFalse(category, pageable);
	}

	public Page<Problem> getProblemsList(Pageable pageable) {

		return problemRepository.findByIsDeletedIsFalse(pageable);
	}

	public Problem getProblem(Long problemId) {

		return problemRepository.findByIdNotDeleted(problemId)
			.orElseThrow(() -> new ProblemException(ProblemExceptionCode.PROBLEM_NOT_FOUND));
	}

	public void removeProblem(Problem problem) {

		problemRepository.delete(problem);

	}
}
