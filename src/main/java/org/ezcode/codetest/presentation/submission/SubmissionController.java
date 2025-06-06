package org.ezcode.codetest.presentation.submission;

import java.util.List;

import org.ezcode.codetest.application.submission.dto.request.review.CodeReviewRequest;
import org.ezcode.codetest.application.submission.dto.request.submission.CodeSubmitRequest;
import org.ezcode.codetest.application.submission.dto.response.review.CodeReviewResponse;
import org.ezcode.codetest.application.submission.dto.response.submission.GroupedSubmissionResponse;
import org.ezcode.codetest.application.submission.service.SubmissionService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SubmissionController {

	private final SubmissionService submissionService;

	@PostMapping("/problems/{problemId}/submit-stream")
	public SseEmitter submitCodeStream(
		@PathVariable Long problemId,
		@RequestBody @Valid CodeSubmitRequest request,
		@AuthenticationPrincipal AuthUser authUser) {
		return submissionService.submitCodeStream(problemId, request, authUser);
	}

	@GetMapping("/submissions")
	public ResponseEntity<List<GroupedSubmissionResponse>> getSubmissions(@AuthenticationPrincipal AuthUser authUser) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(submissionService.getSubmissions(authUser));
	}

	@PostMapping("/problems/{problemId}/review")
	public ResponseEntity<CodeReviewResponse> getCodeReview(
		@PathVariable Long problemId,
		@RequestBody @Valid CodeReviewRequest request) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(submissionService.getCodeReview(problemId, request));
	}
}
