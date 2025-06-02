package org.ezcode.codetest.domain.problem.service;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.repository.ProblemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemDomainService {

	private final ProblemRepository problemRepository;

	public Problem createProblem(Problem problem) {
		return problemRepository.save(problem);
	}

	public Page<Problem> getProblemsByCategoryList(Category category, Pageable pageable) {
		if (category == null) {
			return problemRepository.findAll(pageable); // 전체 조회
		}
		return problemRepository.findByCategory(category, pageable);
	}

	public Page<Problem> getProblemsList(Pageable pageable) {

		return problemRepository.findAll(pageable);
	}

	public Problem getProblem(Long problemId) {

		return problemRepository.findById(problemId)
			.orElseThrow(() -> new EntityNotFoundException("문제를 찾을수 없습니다."));
	}
}
