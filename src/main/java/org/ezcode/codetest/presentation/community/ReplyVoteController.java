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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/problems/{problemId}/discussions/{discussionId}/replies/{replyId}/votes")
@Tag(name = "ReplyVotes", description = "댓글 추천(Vote) 관리 API")
@RequiredArgsConstructor
public class ReplyVoteController {

	private final ReplyVoteService replyVoteService;

	@Operation(
		summary = "댓글 추천 토글",
		description = "댓글에 대한 추천을 생성하거나 취소합니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true),
			@Parameter(name = "discussionId", description = "토론 ID", required = true),
			@Parameter(name = "replyId", description = "댓글 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "201", description = "추천 생성됨 (voteStatus=true)")
	@ApiResponse(responseCode = "200", description = "추천 취소됨 (voteStatus=false)")
	@PostMapping
	public ResponseEntity<VoteResponse> toggleVote(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@PathVariable Long replyId,
		@AuthenticationPrincipal AuthUser authUser
	) {

		VoteResponse response = replyVoteService.validateAndToggleVote(problemId, discussionId, replyId, authUser.getId());
		HttpStatus status = response.voteStatus() ? HttpStatus.CREATED : HttpStatus.OK;

		return ResponseEntity.status(status).body(response);
	}

	@Operation(
		summary = "댓글 추천 상태 조회",
		description = "현재 사용자가 해당 댓글에 추천했는지 여부를 반환합니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true),
			@Parameter(name = "discussionId", description = "토론 ID", required = true),
			@Parameter(name = "replyId", description = "댓글 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "200", description = "VoteResponse 반환")
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
