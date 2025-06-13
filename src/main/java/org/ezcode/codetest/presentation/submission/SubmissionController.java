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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Submission", description = "코드 제출 및 리뷰 관련 API")
public class SubmissionController {

	private final SubmissionService submissionService;

/*	@PostMapping("/problems/{problemId}/submit-stream")
	@Operation(
		summary = "코드 제출 (SSE 응답)",
		description = """
        이 API는 Server-Sent Events(SSE)를 통해 테스트케이스별 채점 결과를 스트리밍으로 전송합니다.

        응답 MIME 타입: `text/event-stream`
        
        응답 예시:
        ```
        data: {"isPassed":true,"expectedOutput":"7","actualOutput":"7","executionTime":0.129,"memoryUsage":12196,"message":"Accepted"}
        ```
        """
	)
	@ApiResponse(
		responseCode = "200",
		description = "SSE로 스트리밍 응답 전송",
		content = @Content(mediaType = "text/event-stream")
	)
	public SseEmitter submitCodeStream(
		@Parameter(description = "제출할 문제 ID", required = true) @PathVariable Long problemId,
		@RequestBody @Valid CodeSubmitRequest request,
		@AuthenticationPrincipal AuthUser authUser) {
		return submissionService.submitCodeStream(problemId, request, authUser);
	}*/

	@PostMapping("/problems/{problemId}/submit-stream-test")
	public SseEmitter submitCodeStreamTest(@PathVariable Long problemId,
		@RequestBody CodeSubmitRequest request,
		@AuthenticationPrincipal AuthUser authUser) {
		return submissionService.enqueueCodeSubmission(problemId, request, authUser);
	}

	@Operation(
		summary = "사용자 제출 목록 조회",
		description = "현재 로그인한 사용자의 코드 제출 기록을 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "제출 목록 반환")
		}
	)
	@GetMapping("/submissions")
	public ResponseEntity<List<GroupedSubmissionResponse>> getSubmissions(@AuthenticationPrincipal AuthUser authUser) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(submissionService.getSubmissions(authUser));
	}

	@Operation(
		summary = "코드 리뷰 요청",
		description = "특정 문제에 대해 사용자의 제출 코드를 리뷰 요청합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "리뷰 결과 반환")
		}
	)
	@PostMapping("/problems/{problemId}/review")
	public ResponseEntity<CodeReviewResponse> getCodeReview(
		@Parameter(description = "문제 ID", required = true) @PathVariable Long problemId,
		@RequestBody @Valid CodeReviewRequest request) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(submissionService.getCodeReview(problemId, request));
	}
}
