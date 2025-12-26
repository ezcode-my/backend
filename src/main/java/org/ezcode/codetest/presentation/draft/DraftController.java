package org.ezcode.codetest.presentation.draft;

import org.ezcode.codetest.application.draft.dto.request.DraftSaveRequest;
import org.ezcode.codetest.application.draft.dto.response.DraftResponse;
import org.ezcode.codetest.application.draft.servcie.DraftService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/drafts")
@RequiredArgsConstructor
@Tag(name = "Drafts", description = "코드 자동 저장 API")
public class DraftController {

	private final DraftService draftService;

	@PostMapping
	public ResponseEntity<DraftResponse> saveDraft(
		@Valid @RequestBody DraftSaveRequest request,
		@AuthenticationPrincipal AuthUser authUser
	) {

		DraftResponse response = draftService.autoSave(authUser.getId(), request);

		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<DraftResponse> getDraft(
		@RequestParam Long problemId,
		@RequestParam Long languageId,
		@AuthenticationPrincipal AuthUser authUser
	) {

		DraftResponse response = draftService.getDraft(
			authUser.getId(),
			problemId,
			languageId
		);

		return ResponseEntity.ok(response);
	}
}
