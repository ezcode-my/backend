package org.ezcode.codetest.presentation.community;

import org.ezcode.codetest.application.community.dto.request.DiscussionCreateRequest;
import org.ezcode.codetest.application.community.dto.request.DiscussionModifyRequest;
import org.ezcode.codetest.application.community.dto.response.DiscussionResponse;
import org.ezcode.codetest.application.community.service.DiscussionService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/problems/{problemId}/discussions")
@Tag(name = "Discussions", description = "문제별 토론 관리 API")
@RequiredArgsConstructor
public class DiscussionController {

	private final DiscussionService discussionService;

	@Operation(
		summary = "토론 생성",
		description = "주어진 문제(problemId)에 새로운 토론을 생성합니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "201", description = "생성된 Discussion 반환")
	@PostMapping
	public ResponseEntity<DiscussionResponse> createDiscussion(
		@PathVariable Long problemId,
		@RequestBody @Valid DiscussionCreateRequest request,
		@AuthenticationPrincipal AuthUser authUser
	) {

		DiscussionResponse dto = discussionService.createDiscussion(problemId, request, authUser.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@Operation(
		summary = "토론 목록 조회",
		description = "해당 문제의 토론 목록을 페이징하여 조회합니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "200", description = "토론 목록 조회 성공")
	@GetMapping
	public ResponseEntity<Page<DiscussionResponse>> getDiscussions(
		@PathVariable Long problemId,
		@PageableDefault Pageable pageable
	) {

		Page<DiscussionResponse> page = discussionService.getDiscussions(problemId, pageable);
		return ResponseEntity.ok(page);
	}

	@Operation(
		summary = "토론 수정",
		description = "특정 토론(discussionId)을 수정합니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true),
			@Parameter(name = "discussionId", description = "토론 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "200", description = "수정된 Discussion 반환")
	@PutMapping("/{discussionId}")
	public ResponseEntity<DiscussionResponse> modifyDiscussion(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@RequestBody @Valid DiscussionModifyRequest request,
		@AuthenticationPrincipal AuthUser authUser
	) {

		DiscussionResponse dto = discussionService.modifyDiscussion(problemId, discussionId, request, authUser.getId());
		return ResponseEntity.ok(dto);
	}

	@Operation(
		summary = "토론 삭제",
		description = "특정 토론(discussionId)을 삭제합니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true),
			@Parameter(name = "discussionId", description = "토론 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "200", description = "삭제 성공")
	@DeleteMapping("/{discussionId}")
	public ResponseEntity<Void> removeDiscussion(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@AuthenticationPrincipal AuthUser authUser
	) {

		discussionService.removeDiscussion(problemId, discussionId, authUser.getId());
		return ResponseEntity.ok().build();
	}
}
