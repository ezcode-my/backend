package org.ezcode.codetest.domain.community.service;

import org.ezcode.codetest.domain.community.repository.DiscussionRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscussionDomainService {

	private final DiscussionRepository discussionRepository;



}
