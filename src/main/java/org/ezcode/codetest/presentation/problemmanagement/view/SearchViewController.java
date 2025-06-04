package org.ezcode.codetest.presentation.problemmanagement.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

//임시 뷰 컨트롤러입니다.

@RequiredArgsConstructor
@Controller
public class SearchViewController {

	@GetMapping("/searching")
	public String getSearchingPage() {

		return "search-page";
	}
}