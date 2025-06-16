package org.ezcode.codetest.presentation.community;

import org.ezcode.codetest.application.community.dto.request.VoteRequest;
import org.ezcode.codetest.application.community.dto.response.VoteResponse;
import org.ezcode.codetest.application.community.service.ReplyVoteService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/problems/{problemId}/discussions/{discussionId}/replies/{replyId}/votes")
@RequiredArgsConstructor
public class ReplyVoteController {

	private final ReplyVoteService replyVoteService;

	@PostMapping
	public ResponseEntity<VoteResponse> vote(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@PathVariable Long replyId,
		@RequestBody VoteRequest request,
		@AuthenticationPrincipal AuthUser authUser
	) {

		VoteResponse response = replyVoteService.manageVoteOnReply(problemId, discussionId, replyId, request, authUser.getId());

		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<VoteResponse> getVoteStatus(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@PathVariable Long replyId,
		@AuthenticationPrincipal AuthUser authUser
	) {

		VoteResponse response = replyVoteService.getVoteStatus(authUser.getId(), replyId);

		return ResponseEntity.ok(response);
	}
}
