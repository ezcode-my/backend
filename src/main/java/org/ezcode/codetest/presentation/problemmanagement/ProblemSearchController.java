package org.ezcode.codetest.presentation.problemmanagement;

import java.util.List;
import java.util.Set;

import org.ezcode.codetest.application.problem.service.ProblemSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/problems/search")
public class ProblemSearchController {

	private final ProblemSearchService searchService;

	@GetMapping("/suggestions")
	public ResponseEntity<Set<String>> getProblemSuggestions(@RequestParam String keyword) {

		return ResponseEntity.status(HttpStatus.OK).body(searchService.getProblemSuggestions(keyword));
	}

	@GetMapping
	public ResponseEntity<List<Long>> getProblemSearchResult(@RequestParam String keyword) {

		return ResponseEntity.status(HttpStatus.OK).body(searchService.getProblemSearchResult(keyword));
	}
}
