package org.ezcode.codetest.presentation.problemmanagement.problem;

import java.util.Set;

import org.ezcode.codetest.domain.problem.model.ProblemSearchCondition;
import org.ezcode.codetest.application.problem.dto.response.ProblemDetailResponse;
import org.ezcode.codetest.application.problem.dto.response.ProblemResponse;
import org.ezcode.codetest.application.problem.service.ProblemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
@Tag(name = "Problem", description = "문제 API")
public class ProblemController {

	private final ProblemService problemService;
	
	@GetMapping("/suggestions")
	@Operation(summary = "검색어 추천 목록 자동 완성", description = "문제의 제목, 설명, 카테고리 한글명 등을 대상으로 검색어 추천 목록을 반환합니다.")
	@ApiResponse(responseCode = "200", description = "검색어 자동 완성 조회 성공")
	public ResponseEntity<Set<String>> getSearchKeywordSuggestions(
		@RequestParam
		@NotBlank(message = "검색어를 입력하세요.")
		@Size(min = 2, max = 25, message = "검색어 길이는 2~25 사이입니다.")
		String keyword
	) {

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(problemService.getSearchKeywordSuggestions(keyword));
	}

	@GetMapping
	@Operation(summary = "문제 목록 조회", description = "문제를 조건에 따라 목록 조회 합니다.")
	@ApiResponse(responseCode = "200", description = "문제 목록 조회 성공")
	public ResponseEntity<Page<ProblemResponse>> getProblemsListWithCondition(
		@ModelAttribute ProblemSearchCondition condition,
		@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
	) {

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(problemService.getProblemWithCondition(pageable, condition));
	}

	@GetMapping("/{problemId}")
	@Operation(summary = "문제 상세조회", description = "문제를 상세조회 합니다.")
	@ApiResponse(responseCode = "200", description = "문제 상세 조회성공")
	public ResponseEntity<ProblemDetailResponse> getProblem(@PathVariable Long problemId) {

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(problemService.getProblem(problemId));
	}
}
