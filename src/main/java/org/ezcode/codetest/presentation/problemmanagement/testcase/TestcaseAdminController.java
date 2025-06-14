package org.ezcode.codetest.presentation.problemmanagement.testcase;

import java.util.List;

import org.ezcode.codetest.application.problem.dto.request.TestcaseCreateRequest;
import org.ezcode.codetest.application.problem.dto.request.TestcaseUpdateRequest;
import org.ezcode.codetest.application.problem.dto.response.TestcaseResponse;
import org.ezcode.codetest.application.problem.service.TestcaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/admin/problems/{problemId}/testcases")
@RequiredArgsConstructor
@Tag(name = "Testcase(관리자)", description = "테스트 케이스 API(관리자용) ")
public class TestcaseAdminController {

	private final TestcaseService testcaseService;

	@PostMapping
	@Operation(summary = "테스트케이스 등록", description = "해당하는 문제에 테스트케이스를 등록합니다.")
	@ApiResponse(responseCode = "201", description = "테스트케이스 생성 성공")
	public ResponseEntity<TestcaseResponse> createTestcase(
		@PathVariable Long problemId,
		@Valid @RequestBody TestcaseCreateRequest request
	) {

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(testcaseService.createTestcase(problemId, request));
	}

	@GetMapping
	@Operation(summary = "테스트케이스 조회", description = "해당하는 문제에 테스트케이스를 조회합니다.")
	@ApiResponse(responseCode = "200", description = "테스트케이스 조회 성공")
	public ResponseEntity<List<TestcaseResponse>> getTestcaseList(@PathVariable Long problemId) {

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(testcaseService.getTestcaseList(problemId));
	}

	@PutMapping("/{testcaseId}")
	@Operation(summary = "테스트케이스 수정", description = "해당하는 문제에 테스트케이스를 수정합니다.")
	@ApiResponse(responseCode = "200", description = "테스트케이스 수정 성공")
	public ResponseEntity<TestcaseResponse> modifyTestcase(
		@PathVariable Long problemId,
		@PathVariable Long testcaseId,
		@Valid @RequestBody TestcaseUpdateRequest request
	) {

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(testcaseService.modifyTestcase(problemId, testcaseId, request));
	}

	@DeleteMapping("/{testcaseId}")
	@Operation(summary = "테스트케이스 삭제", description = "해당하는 문제에 테스트케이스를 삭제합니다.")
	@ApiResponse(responseCode = "204", description = "테스트케이스 삭제 성공")
	public ResponseEntity<Void> removeTestcase(
		@PathVariable Long problemId,
		@PathVariable Long testcaseId
	) {

		testcaseService.removeTestcase(problemId, testcaseId);

		return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.build();
	}
}
