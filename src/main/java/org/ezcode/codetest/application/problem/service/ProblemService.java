package org.ezcode.codetest.application.problem.service;

import org.ezcode.codetest.application.problem.dto.request.ProblemCreateRequest;
import org.ezcode.codetest.domain.problem.model.ProblemSearchCondition;
import org.ezcode.codetest.application.problem.dto.request.ProblemUpdateRequest;
import org.ezcode.codetest.application.problem.dto.response.ProblemDetailResponse;
import org.ezcode.codetest.application.problem.dto.response.ProblemResponse;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
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

	// 문제 생성 ( 관리자 )
	@Transactional
	public ProblemDetailResponse createProblem(ProblemCreateRequest requestDto, AuthUser authUser) {

		User user = userDomainService.getUserById(authUser.getId());

		Problem savedProblem = problemDomainService.createProblem(
			ProblemCreateRequest.toEntity(requestDto, user)
		);

		return ProblemDetailResponse.from(savedProblem);
	}

	// 문제 전체 조회
	@Transactional(readOnly = true)
	public Page<ProblemResponse> getProblemsList(Pageable pageable, ProblemSearchCondition searchCondition) {
		Page<Problem> problems = problemDomainService.getProblemBySearchCondition(pageable, searchCondition);

		return problems.map(ProblemResponse::from); // Entity → DTO 변환
	}

	// 문제 상세 조회
	@Transactional(readOnly = true)
	public ProblemDetailResponse getProblem(Long problemId) {

		Problem findProblem = problemDomainService.getProblem(problemId);

		return ProblemDetailResponse.from(findProblem);
	}

	// 문제 수정 ( 관리자 )
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

	// 문제 삭제 ( 관리자 )
	@Transactional
	public void removeProblem(Long problemId) {

		Problem findProblem = problemDomainService.getProblem(problemId);

		problemDomainService.removeProblem(findProblem);
	}
}

