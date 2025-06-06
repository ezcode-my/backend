package org.ezcode.codetest.domain.community.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.submission.model.entity.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiscussionRepository {

	Discussion save(Discussion discussion);

	Optional<Discussion> findById(Long discussionId);

	Page<Discussion> findAllByProblemId(Long problemId, Pageable pageable);

	void updateDiscussion(Discussion discussion, Language language, String content);

	void deleteDiscussion(Discussion discussion);

}
