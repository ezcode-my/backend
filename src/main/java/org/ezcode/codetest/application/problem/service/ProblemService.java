package org.ezcode.codetest.application.problem.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ezcode.codetest.application.problem.dto.request.CategoryCreateRequest;
import org.ezcode.codetest.application.problem.dto.request.ProblemCreateRequest;
import org.ezcode.codetest.application.problem.dto.request.ProblemUpdateRequest;
import org.ezcode.codetest.application.problem.dto.response.ProblemDetailResponse;
import org.ezcode.codetest.application.problem.dto.response.ProblemResponse;
import org.ezcode.codetest.domain.game.model.character.CategoryStat;
import org.ezcode.codetest.domain.game.util.StatUpdateUtil;
import org.ezcode.codetest.domain.problem.model.ProblemSearchCondition;
import org.ezcode.codetest.domain.problem.model.entity.Category;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.ProblemCategory;
import org.ezcode.codetest.domain.problem.service.ProblemDomainService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.ezcode.codetest.infrastructure.s3.S3Directory;
import org.ezcode.codetest.infrastructure.s3.S3Uploader;
import org.ezcode.codetest.infrastructure.s3.exception.S3Exception;
import org.ezcode.codetest.infrastructure.s3.exception.code.S3ExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProblemService {

	private final ProblemDomainService problemDomainService;
	private final UserDomainService userDomainService;
	private final StatUpdateUtil statUpdateUtil;
	private final S3Uploader s3Uploader;

	@Transactional
	public void createCategory(CategoryCreateRequest requestDto) {

		Category category =problemDomainService.createCategory(requestDto.toCategory());

		statUpdateUtil.save(new CategoryStat(category));
	}

	// 문제 생성 ( 관리자 )
	@Transactional
	public void createProblem(ProblemCreateRequest requestDto, MultipartFile image, AuthUser authUser) {

		User user = userDomainService.getUserById(authUser.getId());

		Problem problem = ProblemCreateRequest.toProblem(requestDto, user);
		Problem savedProblem = problemDomainService.createProblem(problem, requestDto.categories());

		// 문제 이미지 있다면?
		if (image != null && !image.isEmpty()) {
			String imageUrl = uploadImageAfterTransaction(image, savedProblem.getId());
			updateProblemWithImage(savedProblem.getId(), imageUrl);
		}

	}

	// 문제 전체 조회
	@Transactional(readOnly = true)
	public Page<ProblemResponse> getProblemsList(Pageable pageable, ProblemSearchCondition searchCondition) {

		Page<Problem> problemPage = problemDomainService.getProblemBySearchCondition(pageable, searchCondition);
		List<Problem> problems = problemPage.getContent();
		List<ProblemCategory> categories = problemDomainService.getProblemsCategoryList(problems);

		Map<Problem, List<Category>> maps = categories.stream()
			.collect(Collectors.groupingBy(
				ProblemCategory::getProblem,
				Collectors.mapping(ProblemCategory::getCategory, Collectors.toList())
			));

		List<ProblemResponse> responses = problems.stream()
			.map(p -> ProblemResponse.from(p, maps.getOrDefault(p, List.of())))
			.collect(Collectors.toList());

		return new PageImpl<>(responses, pageable, problemPage.getTotalElements());
	}

	// 문제 상세 조회
	@Transactional(readOnly = true)
	public ProblemDetailResponse getProblem(Long problemId) {

		Problem findProblem = problemDomainService.getProblem(problemId);

		List<Category> categoryList = problemDomainService.getProblemCategoryList(findProblem);

		return ProblemDetailResponse.from(findProblem, categoryList);
	}

	// 문제 수정 ( 관리자 )
	@Transactional
	public void modifyProblem(Long problemId, ProblemUpdateRequest request) {

		Problem findProblem = problemDomainService.getProblem(problemId);

		findProblem.update(
			findProblem.getCreator(),
			request.title(),
			request.description(),
			request.difficulty(),
			request.memoryLimit(),
			request.timeLimit(),
			request.reference()
		);

		problemDomainService.updateCategoryAndSearchEngine(findProblem, request.categories());

	}

	// 문제 삭제 ( 관리자 )
	@Transactional
	public void removeProblem(Long problemId) {

		Problem findProblem = problemDomainService.getProblem(problemId);

		problemDomainService.removeProblem(findProblem);
	}

	@Transactional
	public void updateProblemWithImage(Long problemId, String imageUrl) {
		Problem problem = problemDomainService.getProblem(problemId);
		problem.addImage(imageUrl);
	}

	private String uploadImageAfterTransaction(MultipartFile image, Long problemId) {
		try {
			return s3Uploader.upload(image, S3Directory.PROBLEM.getDir());
		} catch (Exception e) {
			log.error("Problem {} 이미지 업로드 실패", problemId, e);
			throw new S3Exception(S3ExceptionCode.S3_UPLOAD_FAILED);
		}
	}
}

