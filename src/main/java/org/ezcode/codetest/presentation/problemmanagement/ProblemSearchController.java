package org.ezcode.codetest.presentation.problemmanagement;

import java.util.List;

import org.ezcode.codetest.application.problem.service.ProblemSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/problems/search")
public class ProblemSearchController {

	private final ProblemSearchService searchService;

	@GetMapping
	public ResponseEntity<List<String>> getProblemSuggestions(String keyword) {

		return ResponseEntity.status(HttpStatus.OK).body(searchService.getProblemSuggestions(keyword));
	}

	@PostMapping ResponseEntity<List<Long>> getProblemSearchResult(String keyword) {

		return ResponseEntity.status(HttpStatus.OK).body(searchService.getProblemSearchResult(keyword));
	}
}
