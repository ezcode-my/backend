package org.ezcode.codetest.presentation;

import java.util.List;

import org.ezcode.codetest.application.problem.dto.request.LanguageCreateRequest;
import org.ezcode.codetest.application.problem.dto.response.LanguageResponse;
import org.ezcode.codetest.application.problem.service.LanguageService;
import org.ezcode.codetest.application.problem.dto.request.LanguageUpdateRequest;
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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/languages")
public class LanguageController {

	private final LanguageService languageService;

	@PostMapping
	public ResponseEntity<LanguageResponse> createLanguage(@RequestBody @Valid LanguageCreateRequest request) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(languageService.createLanguage(request));
	}

	@GetMapping
	public ResponseEntity<List<LanguageResponse>> getLanguages() {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(languageService.getLanguages());
	}

	@PutMapping("/{languageId}")
	public ResponseEntity<LanguageResponse> modifyLanguage(
		@PathVariable Long languageId,
		@RequestBody @Valid LanguageUpdateRequest request) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(languageService.modifyLanguage(languageId, request));
	}

	@DeleteMapping("/{languageId}")
	public ResponseEntity<Void> removeLanguage(@PathVariable Long languageId) {
		languageService.removeLanguage(languageId);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}
}
