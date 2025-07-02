package org.ezcode.codetest.presentation.problemmanagement.problem;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/problems")
public class ProblemSearchController {

	private final ProblemSearchService searchService;

	@Operation(
		summary = "검색어 자동완성 API",
		description = "사용자가 검색창에 2글자 이상 입력시 키워드를 자동완성 하는 기능입니다<search-page 참고해서 제작하시면됩니다>",
		responses = {
			@ApiResponse(responseCode = "200", description = "자동완성되는 키워드들 문자열 반환")
		}
	)
	@GetMapping("/suggestions")
	public ResponseEntity<Set<String>> getProblemSuggestions(
		@RequestParam
		@NotBlank(message = "검색어를 입력하세요.")
		@Size(min = 2, max = 25, message = "검색어 길이는 2~25 사이입니다.")
		String keyword) {

		return ResponseEntity.status(HttpStatus.OK).body(searchService.getProblemSuggestions(keyword));
	}

	@Operation(
		summary = "문제 검색 API",
		description = "사용자가 검색시 해당 검색 결과를 반환합니다.<search-page 참고해서 제작하시면됩니다>",
		responses = {
			@ApiResponse(responseCode = "200", description = "해당 검색어에 매칭되는 대한 문제 반환")
		}
	)
	@GetMapping("/search")
	public ResponseEntity<List<ProblemSearchResponse>> getProblemSearchResult(
		@RequestParam
		@NotBlank(message = "검색어를 입력하세요.")
		@Size(min = 2, max = 25, message = "검색어 길이는 2~25 사이입니다.")
		String keyword) {

		return ResponseEntity.status(HttpStatus.OK).body(searchService.getProblemSearchResult(keyword));
	}
}
