package org.ezcode.codetest.application.problem.service;

import java.util.List;

import org.ezcode.codetest.application.problem.dto.request.ProblemCreateRequest;
import org.ezcode.codetest.application.problem.dto.response.ProblemDetailResponse;
import org.ezcode.codetest.application.problem.dto.response.ProblemResponse;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.service.ProblemDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemService {

	private final ProblemDomainService problemDomainService;

	@Transactional
	public ProblemDetailResponse createProblem(ProblemCreateRequest requestDto) {

		// 유저 정보가 없어서 임시로 테스트용
		User user = User.builder()
			.email("이메일")
			.nickname("닉네임^^")
			.build();
		// user.setId(1L); // User Entity Setter 필요

		Problem savedProblem = problemDomainService.saveProblem(
			ProblemCreateRequest.toEntity(requestDto, user)
		);

		return ProblemDetailResponse.from(savedProblem);
	}

	public Page<ProblemResponse> findAllByCategory(Pageable pageable, Category category) {
		Page<Problem> problems;

		if (category != null) {
			problems = problemDomainService.findByCategory(category, pageable);
		} else {
			problems = problemDomainService.findAll(pageable);
		}

		return problems.map(ProblemResponse::from); // Entity → DTO 변환
	}

	public ProblemDetailResponse findByIdProblem(Long id) {

		Problem findProblem = problemDomainService.findByIdOrElseThrow(id);

		return ProblemDetailResponse.from(findProblem);
	}

}
