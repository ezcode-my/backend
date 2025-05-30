package org.ezcode.codetest.presentation;

import java.util.List;

import org.ezcode.codetest.application.language.LanguageRequest;
import org.ezcode.codetest.application.language.LanguageResponse;
import org.ezcode.codetest.application.language.LanguageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/language")
public class LanguageController {

	private final LanguageService languageService;

	@PostMapping
	public ResponseEntity<LanguageResponse> createLanguage(@RequestBody LanguageRequest request) {
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

	@DeleteMapping("/{languageId}")
	public ResponseEntity<Void> deleteLanguage(@PathVariable Long languageId) {
		languageService.deleteLanguage(languageId);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}
}
