package org.ezcode.codetest.presentation.community;

import org.ezcode.codetest.application.community.dto.request.ReplyCreateRequest;
import org.ezcode.codetest.application.community.dto.request.ReplyModifyRequest;
import org.ezcode.codetest.application.community.dto.response.ReplyResponse;
import org.ezcode.codetest.application.community.service.ReplyService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springdoc.core.annotations.ParameterObject;
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
@RequestMapping("/api/problems/{problemId}/discussions/{discussionId}/replies")
@Tag(name = "Replies", description = "토론 댓글(Reply) 관리 API")
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyService replyService;

	@Operation(
		summary = "댓글 생성",
		description = "주어진 토론에 댓글을 추가합니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true),
			@Parameter(name = "discussionId", description = "토론 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "201", description = "생성된 Reply 반환")
	@PostMapping
	public ResponseEntity<ReplyResponse> createReply(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@RequestBody @Valid ReplyCreateRequest request,
		@AuthenticationPrincipal AuthUser authUser
	) {

		ReplyResponse dto = replyService.createReply(problemId, discussionId, request, authUser.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@Operation(
		summary = "댓글 목록 조회",
		description = "해당 토론의 최상위 댓글 목록을 페이징하여 조회합니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true),
			@Parameter(name = "discussionId", description = "토론 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공")
	@GetMapping
	public ResponseEntity<Page<ReplyResponse>> getReplies(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@ParameterObject @PageableDefault Pageable pageable,
		@AuthenticationPrincipal AuthUser authUser
	) {

		Long currentUserId = (authUser != null ? authUser.getId() : null);

		Page<ReplyResponse> page = replyService.getReplies(problemId, discussionId, currentUserId, pageable);
		return ResponseEntity.ok(page);
	}

	@Operation(
		summary = "대댓글 목록 조회",
		description = "특정 부모 댓글(parentReplyId)의 하위 댓글을 페이징하여 조회합니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true),
			@Parameter(name = "discussionId", description = "토론 ID", required = true),
			@Parameter(name = "parentReplyId", description = "부모 댓글 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "200", description = "대댓글 목록 조회 성공")
	@GetMapping("/{parentReplyId}")
	public ResponseEntity<Page<ReplyResponse>> getReplies(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@PathVariable Long parentReplyId,
		@ParameterObject @PageableDefault Pageable pageable,
		@AuthenticationPrincipal AuthUser authUser
	) {

		Long currentUserId = (authUser != null ? authUser.getId() : null);

		Page<ReplyResponse> page = replyService.getChildReplies(problemId, discussionId, parentReplyId, currentUserId, pageable);
		return ResponseEntity.ok(page);
	}

	@Operation(
		summary = "댓글 수정",
		description = "특정 댓글(replyId)을 수정합니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true),
			@Parameter(name = "discussionId", description = "토론 ID", required = true),
			@Parameter(name = "replyId", description = "댓글 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "200", description = "수정된 Reply 반환")
	@PutMapping("/{replyId}")
	public ResponseEntity<ReplyResponse> modifyReply(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@PathVariable Long replyId,
		@RequestBody @Valid ReplyModifyRequest request,
		@AuthenticationPrincipal AuthUser authUser
	) {

		ReplyResponse dto = replyService.modifyReply(problemId, discussionId, replyId, request, authUser.getId());
		return ResponseEntity.ok(dto);
	}

	@Operation(
		summary = "댓글 삭제",
		description = "특정 댓글(replyId)을 삭제합니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true),
			@Parameter(name = "discussionId", description = "토론 ID", required = true),
			@Parameter(name = "replyId", description = "댓글 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "200", description = "삭제 성공")
	@DeleteMapping("/{replyId}")
	public ResponseEntity<Void> removeReply(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@PathVariable Long replyId,
		@AuthenticationPrincipal AuthUser authUser
	) {

		replyService.removeReply(problemId, discussionId, replyId, authUser.getId());
		return ResponseEntity.ok().build();
	}
}
