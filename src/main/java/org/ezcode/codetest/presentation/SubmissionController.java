package org.ezcode.codetest.presentation;

import org.ezcode.codetest.application.submission.dto.request.SubmitRequest;
import org.ezcode.codetest.application.submission.dto.response.SubmitResponse;
import org.ezcode.codetest.application.submission.service.SubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
		@RequestBody @Valid SubmitRequest request) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(submissionService.submitCode(problemId, request));
	}
}
