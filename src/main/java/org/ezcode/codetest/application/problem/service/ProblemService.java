package org.ezcode.codetest.application.problem.service;

import org.ezcode.codetest.application.problem.dto.request.ProblemCreateRequest;
import org.ezcode.codetest.application.problem.dto.request.ProblemUpdateRequest;
import org.ezcode.codetest.application.problem.dto.response.ProblemDetailResponse;
import org.ezcode.codetest.application.problem.dto.response.ProblemResponse;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.service.ProblemDomainService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemService {

	private final ProblemDomainService problemDomainService;
	private final UserDomainService userDomainService;

	@Transactional
	public ProblemDetailResponse createProblem(ProblemCreateRequest requestDto, AuthUser authUser) {

		User user = userDomainService.getUserById(authUser.getId());

		Problem savedProblem = problemDomainService.createProblem(
			ProblemCreateRequest.toEntity(requestDto, user)
		);

		return ProblemDetailResponse.from(savedProblem);
	}

	@Transactional(readOnly = true)
	public Page<ProblemResponse> getProblemsList(Pageable pageable, Category category) {
		Page<Problem> problems;

		if (category != null) {
			problems = problemDomainService.getProblemsByCategoryList(category, pageable);
		} else {
			problems = problemDomainService.getProblemsList(pageable);
		}

		return problems.map(ProblemResponse::from); // Entity → DTO 변환
	}

	@Transactional(readOnly = true)
	public ProblemDetailResponse getProblem(Long problemId) {

		Problem findProblem = problemDomainService.getProblem(problemId);

		return ProblemDetailResponse.from(findProblem);
	}

	@Transactional
	public ProblemDetailResponse modifyProblem(Long problemId, ProblemUpdateRequest request) {

		Problem findProblem = problemDomainService.getProblem(problemId);

		findProblem.update(
			findProblem.getCreator(),
			request.category(),
			request.title(),
			request.description(),
			request.difficulty(),
			request.memoryLimit(),
			request.timeLimit(),
			request.reference()
		);

		return ProblemDetailResponse.from(findProblem);
	}

	@Transactional
	public void removeProblem(Long problemId) {

		Problem findProblem = problemDomainService.getProblem(problemId);

		problemDomainService.removeProblem(findProblem);
	}
}

