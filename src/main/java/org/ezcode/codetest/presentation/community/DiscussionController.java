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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/problems/{problemId}/discussions")
@Tag(name = "Discussions", description = "문제별 토론글 관리 API")
@RequiredArgsConstructor
public class DiscussionController {

	private final DiscussionService discussionService;

	@Operation(
		summary = "토론 생성",
		description = "주어진 문제 ID에 새로운 Discussion을 생성합니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "201", description = "생성된 DiscussionResponse 반환")
	@PostMapping
	public ResponseEntity<DiscussionResponse> createDiscussion(
		@PathVariable Long problemId,
		@RequestBody @Valid DiscussionCreateRequest request,
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(discussionService.createDiscussion(problemId, request, authUser.getId()));
	}

	@Operation(
		summary = "토론 목록 조회",
		description = "문제별 Discussion 목록을 정렬(sortBy) 및 페이징(pageable)하여 조회합니다. 로그인하지 않은 상태로도 조회할 수 있습니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true),
			@Parameter(name = "sortBy", description = "정렬 기준 (best, upvote, latest), best: upvote-downvote 순으로 정렬", example = "best", required = false)
		}
	)
	@ApiResponse(responseCode = "200", description = "토론 목록 조회 성공")
	@GetMapping
	public ResponseEntity<Page<DiscussionResponse>> getDiscussions(
		@PathVariable Long problemId,
		@RequestParam(defaultValue = "best") String sortBy,
		@PageableDefault Pageable pageable,
		@AuthenticationPrincipal AuthUser authUser
	) {

		Long currentUserId = (authUser != null ? authUser.getId() : null);

		return ResponseEntity
			.ok()
			.body(discussionService.getDiscussions(problemId, sortBy, currentUserId, pageable));
	}

	@Operation(
		summary = "토론 수정",
		description = "특정 Discussion을 수정합니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true),
			@Parameter(name = "discussionId", description = "토론 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "200", description = "수정된 DiscussionResponse 반환")
	@PutMapping("/{discussionId}")
	public ResponseEntity<DiscussionResponse> modifyDiscussion(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@RequestBody @Valid DiscussionModifyRequest request,
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity
			.ok()
			.body(discussionService.modifyDiscussion(problemId, discussionId, request, authUser.getId()));
	}

	@Operation(
		summary = "토론 삭제",
		description = "특정 Discussion을 삭제합니다.",
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
