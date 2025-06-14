package org.ezcode.codetest.presentation.problemmanagement.problem;

import org.ezcode.codetest.application.problem.dto.request.ProblemCreateRequest;
import org.ezcode.codetest.application.problem.dto.request.ProblemUpdateRequest;
import org.ezcode.codetest.application.problem.dto.response.ProblemDetailResponse;
import org.ezcode.codetest.application.problem.service.ProblemService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/problems")
@RequiredArgsConstructor
@Tag(name = "Problem(관리자)", description = "문제 API(관리자용)")
public class ProblemAdminController {

	private final ProblemService problemService;

	@PostMapping
	@Operation(summary = "문제 등록", description = "문제를 등록합니다.")
	@ApiResponse(responseCode = "201", description = "문제 생성 성공")
	public ResponseEntity<ProblemDetailResponse> createProblem(
		@Valid @RequestBody ProblemCreateRequest request,
		@AuthenticationPrincipal AuthUser user
	) {

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(problemService.createProblem(request, user));
	}

	@PutMapping("/{problemId}")
	@Operation(summary = "문제 수정", description = "문제를 수정합니다.")
	@ApiResponse(responseCode = "200", description = "문제 수정 성공")
	public ResponseEntity<ProblemDetailResponse> modifyProblem(
		@PathVariable Long problemId,
		@Valid @RequestBody ProblemUpdateRequest request
	) {

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(problemService.modifyProblem(problemId, request));
	}

	@DeleteMapping("/{problemId}")
	@Operation(summary = "문제 삭제", description = "문제를 삭제합니다.")
	@ApiResponse(responseCode = "204", description = "문제 삭제 성공")
	public ResponseEntity<Void> removeProblem(@PathVariable Long problemId) {

		problemService.removeProblem(problemId);

		return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.build();
	}
}
