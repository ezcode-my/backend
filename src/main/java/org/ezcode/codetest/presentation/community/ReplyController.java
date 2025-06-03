package org.ezcode.codetest.presentation.community;

import org.ezcode.codetest.application.community.dto.request.ReplyCreateRequest;
import org.ezcode.codetest.application.community.dto.request.ReplyModifyRequest;
import org.ezcode.codetest.application.community.dto.response.ReplyResponse;
import org.ezcode.codetest.application.community.service.ReplyService;
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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/problems/{problemId}/discussions/{discussionId}/replies")
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyService replyService;

	@PostMapping
	public ResponseEntity<ReplyResponse> createReply(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@RequestBody @Valid ReplyCreateRequest request,
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(replyService.createReply(problemId, discussionId, request, authUser.getId()));
	}

	// 댓글 목록 조회
	@GetMapping
	public ResponseEntity<Page<ReplyResponse>> getReplies(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@PageableDefault Pageable pageable
	) {
		return ResponseEntity
			.ok()
			.body(replyService.getReplies(problemId, discussionId, pageable));
	}

	// 대댓글 목록 조회
	@GetMapping("/{parentReplyId}")
	public ResponseEntity<Page<ReplyResponse>> getReplies(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@PathVariable Long parentReplyId,
		@PageableDefault Pageable pageable
	) {
		return ResponseEntity
			.ok()
			.body(replyService.getChildReplies(problemId, discussionId, parentReplyId, pageable));
	}

	@PutMapping("/{replyId}")
	public ResponseEntity<ReplyResponse> modifyReply(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@PathVariable Long replyId,
		@RequestBody @Valid ReplyModifyRequest request,
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity
			.ok()
			.body(replyService.modifyReply(problemId, discussionId, replyId, request, authUser.getId()));
	}

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
