package org.ezcode.codetest.presentation.problemmanagement.problem;

import org.ezcode.codetest.application.problem.dto.request.ProblemSearchCondition;
import org.ezcode.codetest.application.problem.dto.response.ProblemDetailResponse;
import org.ezcode.codetest.application.problem.dto.response.ProblemResponse;
import org.ezcode.codetest.application.problem.service.ProblemService;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/problems")
@RequiredArgsConstructor
@Tag(name = "Problem", description = "문제 API")
public class ProblemController {

	private final ProblemService problemService;

	@GetMapping
	@Operation(summary = "문제 전체조회", description = "문제를 전체조회 합니다.")
	@ApiResponse(responseCode = "200", description = "문제 전체 조회성공")
	public ResponseEntity<Page<ProblemResponse>> getProblemsList(
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		@RequestParam(required = false) Category category,
		@RequestParam(required = false) Difficulty difficulty,
		@RequestParam(required = false) String title
	) {

		ProblemSearchCondition searchCondition = new ProblemSearchCondition(category, difficulty, title);

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(problemService.getProblemsList(pageable, searchCondition));
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
