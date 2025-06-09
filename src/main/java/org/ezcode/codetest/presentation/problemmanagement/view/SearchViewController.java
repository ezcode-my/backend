package org.ezcode.codetest.presentation.problemmanagement.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SearchViewController {

	@GetMapping("/searching")
	public String getSearchingPage() {

		return "search-page";
	}
}