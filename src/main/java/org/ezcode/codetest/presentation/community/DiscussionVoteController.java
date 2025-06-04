package org.ezcode.codetest.presentation.community;

import org.ezcode.codetest.application.community.dto.response.VoteResponse;
import org.ezcode.codetest.application.community.service.DiscussionVoteService;
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
@RequestMapping("/problems/{problemId}/discussions/{discussionId}/votes")
@RequiredArgsConstructor
public class DiscussionVoteController {

	private final DiscussionVoteService discussionVoteService;

	@PostMapping
	public ResponseEntity<VoteResponse> toggleVote(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@AuthenticationPrincipal AuthUser authUser
	) {
		VoteResponse response = discussionVoteService.validateAndToggleVote(problemId, discussionId, authUser.getId());
		HttpStatus status = response.voteStatus() ? HttpStatus.CREATED : HttpStatus.OK;

		return ResponseEntity
			.status(status)
			.body(response);
	}

	@GetMapping
	public ResponseEntity<VoteResponse> getVoteStatus(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@AuthenticationPrincipal AuthUser authUser
	) {
		VoteResponse response = discussionVoteService.getVoteStatus(authUser.getId(), discussionId);
		return ResponseEntity
			.ok()
			.body(response);
	}
}
