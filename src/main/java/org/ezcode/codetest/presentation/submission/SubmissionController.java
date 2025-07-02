package org.ezcode.codetest.presentation.submission;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;

import org.ezcode.codetest.application.submission.dto.request.review.CodeReviewRequest;
import org.ezcode.codetest.application.submission.dto.request.submission.CodeSubmitRequest;
import org.ezcode.codetest.application.submission.dto.response.review.CodeReviewResponse;
import org.ezcode.codetest.application.submission.dto.response.submission.GroupedSubmissionResponse;
import org.ezcode.codetest.application.submission.service.SubmissionService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Submission", description = "코드 제출 및 리뷰 관련 API")
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping("/problems/{problemId}/submit-ws")
    @Operation(
        summary = "코드 제출 (WebSocket)",
        description = """
        문제에 대한 코드를 제출하면 채점 큐에 등록되고,
        서버는 WebSocket(STOMP)을 통해 채점 결과를 실시간으로 전송합니다.

        반환된 sessionKey를 사용해 다음 경로로 구독하세요.
        
        • /user/queue/submission/{sessionKey}/init
        
        • /user/queue/submission/{sessionKey}/case
        
        • /user/queue/submission/{sessionKey}/final
        
        • /topic/submission/{sessionKey}/error
        
        • /user/queue/submission/{sessionKey}/git-status
        """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "코드 제출 성공 및 sessionKey 반환"),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터"),
        @ApiResponse(responseCode = "409", description = "이미 해당 문제를 채점 중인 경우"),
    })
    public ResponseEntity<HashSet<String>> submitCodeStream(
        @Parameter(description = "제출할 문제 ID", required = true) @PathVariable Long problemId,
        @RequestBody @Valid CodeSubmitRequest request,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        HashSet<String> set = new HashSet<>();
        set.add(submissionService.enqueueCodeSubmission(problemId, request, authUser));
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(set);
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
        @RequestBody @Valid CodeReviewRequest request,
        @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(submissionService.getCodeReview(problemId, request, authUser));
    }
}
