package org.ezcode.codetest.presentation.problemmanagement;

import org.ezcode.codetest.application.problem.dto.request.ProblemCreateRequest;
import org.ezcode.codetest.application.problem.dto.request.ProblemUpdateRequest;
import org.ezcode.codetest.application.problem.dto.response.ProblemDetailResponse;
import org.ezcode.codetest.application.problem.service.ProblemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/problems")
@RequiredArgsConstructor
public class ProblemAdminController {

	private final ProblemService problemService;

	// @PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ProblemDetailResponse> createProblem(@Valid @RequestBody ProblemCreateRequest request) {

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(problemService.createProblem(request));
	}

	@PutMapping("/{problemId}")
	public ResponseEntity<ProblemDetailResponse> modifyProblem(
		@PathVariable Long problemId,
		@Valid @RequestBody ProblemUpdateRequest request
	) {

		return ResponseEntity.status(HttpStatus.OK).body(problemService.modifyProblem(problemId, request));
	}
}
