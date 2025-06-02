package org.ezcode.codetest.presentation.problemmanagement.testcase;

import org.ezcode.codetest.application.problem.service.TestcaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/problems/{problemId}/testcases")
@RequiredArgsConstructor
public class TestcaseController {

	private final TestcaseService testcaseService;
}
