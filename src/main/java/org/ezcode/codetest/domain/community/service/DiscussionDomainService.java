package org.ezcode.codetest.domain.community.service;

import org.ezcode.codetest.domain.community.exception.CommunityException;
import org.ezcode.codetest.domain.community.exception.CommunityExceptionCode;
import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.community.repository.DiscussionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscussionDomainService {

	private final DiscussionRepository discussionRepository;

	public Discussion store(Discussion discussion) {
		return discussionRepository.save(discussion);
	}

	public Discussion retrieveById(Long discussionId) {
		return discussionRepository.findById(discussionId)
			.orElseThrow(() -> new CommunityException(CommunityExceptionCode.DISCUSSION_NOT_FOUND));
	}

	public Page<Discussion> retrieveAllByProblemId(Long problemId, Pageable pageable) {
		return discussionRepository.findAllByProblemId(problemId, pageable);
	}

	public void remove(Discussion discussion) {
		discussion.setDeleted();
		store(discussion);
	}

}
