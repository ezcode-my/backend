package org.ezcode.codetest.presentation;

import java.util.List;

import org.ezcode.codetest.application.submission.dto.request.SubmitRequest;
import org.ezcode.codetest.application.submission.dto.response.SubmissionHistoryResponse;
import org.ezcode.codetest.application.submission.dto.response.SubmitResponse;
import org.ezcode.codetest.application.submission.service.SubmissionService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/submissions")
public class SubmissionController {

	private final SubmissionService submissionService;

	@PostMapping("/{problemId}")
	public ResponseEntity<SubmitResponse> submitCode(
		@PathVariable Long problemId,
		@RequestBody @Valid SubmitRequest request,
		@AuthenticationPrincipal AuthUser authUser) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(submissionService.submitCode(problemId, request, authUser));
	}

	@GetMapping
	public ResponseEntity<List<SubmissionHistoryResponse>> getSubmissions(@AuthenticationPrincipal AuthUser authUser) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(submissionService.getSubmissions(authUser));
	}
}
