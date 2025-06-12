package org.ezcode.codetest.domain.community.service;

import org.ezcode.codetest.domain.community.exception.CommunityException;
import org.ezcode.codetest.domain.community.exception.CommunityExceptionCode;
import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.community.repository.DiscussionRepository;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscussionDomainService {

	private final DiscussionRepository discussionRepository;

	public Discussion createDiscussion(Discussion discussion) {

		return discussionRepository.save(discussion);
	}

	public Discussion getDiscussionById(Long discussionId) {

		return discussionRepository.findById(discussionId)
			.orElseThrow(() -> new CommunityException(CommunityExceptionCode.DISCUSSION_NOT_FOUND));
	}

	public Discussion getAndValidateDiscussionForProblem(Long discussionId, Long problemId) {

		Discussion discussion = getDiscussionById(discussionId);
		validateProblemMatches(discussion, problemId);

		return discussion;
	}

	public Page<Discussion> getAllDiscussionsByProblemId(Long problemId, Pageable pageable) {

		return discussionRepository.findAllByProblemId(problemId, pageable);
	}

	public void modify(Discussion discussion, Language language, String content) {

		discussionRepository.updateDiscussion(discussion, language, content);
	}

	public void remove(Discussion discussion) {

		discussionRepository.deleteDiscussion(discussion);
	}


	public void validateProblemMatches(Discussion discussion, Long problemId) {

		if (!discussion.isProblemMatches(problemId)) {
			throw new CommunityException(CommunityExceptionCode.DISCUSSION_PROBLEM_MISMATCH);
		}
	}

	public void validateIsAuthor(Discussion discussion, Long userId) {

		if (!discussion.isAuthor(userId)) {
			throw new CommunityException(CommunityExceptionCode.USER_NOT_AUTHOR);
		}
	}
}
