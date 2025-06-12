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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/problems/{problemId}/discussions/{discussionId}/votes")
@Tag(name = "DiscussionVotes", description = "토론 추천(Vote) 관리 API")
@RequiredArgsConstructor
public class DiscussionVoteController {

	private final DiscussionVoteService discussionVoteService;

	@Operation(
		summary = "토론 추천 토글",
		description = "토론에 대한 추천을 생성하거나 취소합니다. 이 부분은 추후 추천/비추천 기능으로 변경될 수 있습니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true),
			@Parameter(name = "discussionId", description = "토론 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "201", description = "추천 생성됨 (voteStatus=true)")
	@ApiResponse(responseCode = "200", description = "추천 취소됨 (voteStatus=false)")
	@PostMapping
	public ResponseEntity<VoteResponse> toggleVote(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@AuthenticationPrincipal AuthUser authUser
	) {

		VoteResponse response = discussionVoteService.toggleVoteOnDiscussion(problemId, discussionId, authUser.getId());
		HttpStatus status = response.voteStatus() ? HttpStatus.CREATED : HttpStatus.OK;

		return ResponseEntity
			.status(status)
			.body(response);
	}

	@Operation(
		summary = "토론 추천 상태 조회",
		description = "현재 사용자가 해당 토론에 추천했는지 여부를 반환합니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true),
			@Parameter(name = "discussionId", description = "토론 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "200", description = "VoteResponse 반환")
	@GetMapping
	public ResponseEntity<VoteResponse> getVoteStatus(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@AuthenticationPrincipal AuthUser authUser
	) {

		VoteResponse response = discussionVoteService.getVoteStatus(authUser.getId(), discussionId);
		return ResponseEntity.ok(response);
	}
}
