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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/problems/{problemId}/discussions")
@RequiredArgsConstructor
public class DiscussionController {

	private final DiscussionService discussionService;

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
