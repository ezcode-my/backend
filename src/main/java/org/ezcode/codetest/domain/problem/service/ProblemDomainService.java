package org.ezcode.codetest.domain.problem.service;

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

	public Problem saveProblem(Problem problem) {
		return problemRepository.save(problem);
	}

	public Page<Problem> findByCategory(Category category, Pageable pageable) {
		if (category == null) {
			return problemRepository.findAll(pageable); // 전체 조회
		}
		return problemRepository.findByCategory(category, pageable);
	}

	public Page<Problem> findAll(Pageable pageable) {

		return problemRepository.findAll(pageable);
	}

	public Problem findByIdOrElseThrow(Long id) {

		return problemRepository.findByIdOrElseThrow(id);
	}
}
