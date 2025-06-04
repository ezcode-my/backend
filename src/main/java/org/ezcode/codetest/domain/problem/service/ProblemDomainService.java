package org.ezcode.codetest.domain.problem.service;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.repository.ProblemRepository;
import org.ezcode.codetest.domain.problem.repository.ProblemDocumentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemDomainService {

	private final ProblemRepository problemRepository;
	private final ProblemDocumentRepository searchRepository;

	//저장시 DB 뿐만 아니라 ElasticCache 에도 같이 저장합니다!
	public Problem createProblem(Problem problem) {

		Problem savedProblem = problemRepository.save(problem);

		searchRepository.save(ProblemSearchDocument.from(savedProblem));

		return savedProblem;
	}

	public Page<Problem> getProblemsByCategoryList(Category category, Pageable pageable) {

		return problemRepository.findByCategoryAndIsDeletedIsFalse(category, pageable);
	}

	public Page<Problem> getProblemsList(Pageable pageable) {

		return problemRepository.findByIsDeletedIsFalse(pageable);
	}

	public Problem getProblem(Long problemId) {

		return problemRepository.findByIdNotDeleted(problemId)
			.orElseThrow(() -> new EntityNotFoundException("문제를 찾을수 없습니다."));
	}

	public void removeProblem(Problem problem) {

		problemRepository.delete(problem);

	}
}
