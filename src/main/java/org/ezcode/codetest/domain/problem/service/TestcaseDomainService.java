package org.ezcode.codetest.domain.problem.service;

import org.ezcode.codetest.domain.problem.repository.TestcaseRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestcaseDomainService {

	private final TestcaseRepository testcaseRepository;

}
