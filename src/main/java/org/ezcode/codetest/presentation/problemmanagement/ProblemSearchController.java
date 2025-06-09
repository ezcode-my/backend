package org.ezcode.codetest.presentation.problemmanagement;

import java.util.List;
import java.util.Set;

import org.ezcode.codetest.application.problem.service.ProblemSearchService;
import org.ezcode.codetest.application.problem.dto.response.ProblemSearchResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/problems/search")
public class ProblemSearchController {

	private final ProblemSearchService searchService;

	@GetMapping("/suggestions")
	public ResponseEntity<Set<String>> getProblemSuggestions(
		@RequestParam
		@NotBlank(message = "검색어를 입력하세요.")
		@Size(min = 2, max = 25, message = "검색어 길이는 2~25 사이입니다.")
		String keyword) {

		return ResponseEntity.status(HttpStatus.OK).body(searchService.getProblemSuggestions(keyword));
	}

	@GetMapping
	public ResponseEntity<List<ProblemSearchResponse>> getProblemSearchResult(
		@RequestParam
		@NotBlank(message = "검색어를 입력하세요.")
		@Size(min = 2, max = 25, message = "검색어 길이는 2~25 사이입니다.")
		String keyword) {

		return ResponseEntity.status(HttpStatus.OK).body(searchService.getProblemSearchResult(keyword));
	}
}
