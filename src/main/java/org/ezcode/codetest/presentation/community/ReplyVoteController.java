package org.ezcode.codetest.presentation.community;

import org.ezcode.codetest.application.community.dto.response.VoteResponse;
import org.ezcode.codetest.application.community.service.ReplyVoteService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/problems/{problemId}/discussions/{discussionId}/replies/{replyId}/votes")
@RequiredArgsConstructor
public class ReplyVoteController {

	private final ReplyVoteService replyVoteService;

	@PostMapping
	public ResponseEntity<VoteResponse> toggleVote(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@PathVariable Long replyId,
		@AuthenticationPrincipal AuthUser authUser
	) {
		VoteResponse response = replyVoteService.toggleVoteOnReply(problemId, discussionId, replyId, authUser.getId());
		HttpStatus status = response.voteStatus() ? HttpStatus.CREATED : HttpStatus.OK;

		return ResponseEntity
			.status(status)
			.body(response);
	}

	@GetMapping
	public ResponseEntity<VoteResponse> getVoteStatus(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@PathVariable Long replyId,
		@AuthenticationPrincipal AuthUser authUser
	) {
		VoteResponse response = replyVoteService.getVoteStatus(authUser.getId(), replyId);
		return ResponseEntity
			.ok()
			.body(response);
	}
}
