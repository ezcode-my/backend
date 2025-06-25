package org.ezcode.codetest.domain.community.repository;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.community.dto.DiscussionQueryResult;
import org.ezcode.codetest.domain.community.model.entity.Discussion;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.springframework.data.domain.Pageable;

public interface DiscussionRepository {

	Discussion save(Discussion discussion);

	Optional<Discussion> findById(Long discussionId);

	List<Long> findDiscussionIdsByProblemId(Long problemId, String sortBy, Pageable pageable);

	List<DiscussionQueryResult> findDiscussionsByIds(List<Long> discussionIds, Long currentUserId);

	void updateDiscussion(Discussion discussion, Language language, String content);

	void deleteDiscussion(Discussion discussion);

	Long countByProblemId(Long problemId);

}
