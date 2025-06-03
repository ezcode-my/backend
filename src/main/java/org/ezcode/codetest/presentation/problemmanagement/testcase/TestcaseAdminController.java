package org.ezcode.codetest.presentation.problemmanagement.testcase;

import org.ezcode.codetest.application.problem.dto.request.TestcaseCreateRequest;
import org.ezcode.codetest.application.problem.dto.response.TestcaseResponse;
import org.ezcode.codetest.application.problem.service.TestcaseService;
import org.ezcode.codetest.common.annotation.Auth;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
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
@RequestMapping("/admin/problems/{problemId}/testcases")
@RequiredArgsConstructor
// @PreAuthorize("hasRole('ADMIN')") // 관리자만 가능
public class TestcaseAdminController {

	private final TestcaseService testcaseService;

	@PostMapping
	public ResponseEntity<TestcaseResponse> createTestcase(
		@PathVariable Long problemId,
		@Valid @RequestBody TestcaseCreateRequest request
	) {

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(testcaseService.createTestcase(problemId, request));
	}


}
