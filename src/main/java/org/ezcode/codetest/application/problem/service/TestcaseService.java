package org.ezcode.codetest.application.problem.service;

import org.ezcode.codetest.domain.problem.service.TestcaseDomainService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestcaseService {

	private final TestcaseDomainService testcaseDomainService;

}
