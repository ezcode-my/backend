package org.ezcode.codetest.presentation.community;

import org.ezcode.codetest.application.community.dto.request.DiscussionCreateRequest;
import org.ezcode.codetest.application.community.dto.request.DiscussionUpdateRequest;
import org.ezcode.codetest.application.community.dto.response.DiscussionResponse;
import org.ezcode.codetest.application.community.service.DiscussionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/problems/{problemId}/discussions")
@RequiredArgsConstructor
public class DiscussionController {

	private final DiscussionService discussionService;

	@PostMapping
	public ResponseEntity<DiscussionResponse> createDiscussion(
		@PathVariable Long problemId,
		@RequestBody DiscussionCreateRequest request
	) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(discussionService.createDiscussion(problemId, request));
	}

	@GetMapping
	public ResponseEntity<Page<DiscussionResponse>> getDiscussions(
		@PathVariable Long problemId,
		@PageableDefault Pageable pageable
	) {
		return ResponseEntity
			.ok()
			.body(discussionService.getDiscussions(problemId, pageable));
	}

	@PutMapping("/{discussionId}")
	public ResponseEntity<DiscussionResponse> updateDiscussion(
		@PathVariable Long problemId,
		@PathVariable Long discussionId,
		@RequestBody DiscussionUpdateRequest request
	) {
		return ResponseEntity
			.ok()
			.body(discussionService.updateDiscussion(problemId, discussionId, request));
	}

	@DeleteMapping("/{discussionId}")
	public ResponseEntity<Void> deleteDiscussion(
		@PathVariable Long problemId,
		@PathVariable Long discussionId
	) {
		discussionService.deleteDiscussion(discussionId);
		return ResponseEntity.ok().build();
	}
}
