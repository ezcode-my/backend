package org.ezcode.codetest.presentation.problemmanagement;

import org.ezcode.codetest.application.problem.dto.response.ProblemDetailResponse;
import org.ezcode.codetest.application.problem.dto.response.ProblemResponse;
import org.ezcode.codetest.application.problem.service.ProblemService;
import org.ezcode.codetest.domain.problem.model.enums.Category;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/problems")
@RequiredArgsConstructor
public class ProblemController {

	private final ProblemService problemService;

	@GetMapping
	public ResponseEntity<Page<ProblemResponse>> getProblemsList(
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		@RequestParam(required = false) Category category
	) {

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(problemService.getProblemsList(pageable, category));
	}

	@GetMapping("/{problemId}")
	public ResponseEntity<ProblemDetailResponse> getProblem(@PathVariable Long problemId) {

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(problemService.getProblem(problemId));
	}
}
