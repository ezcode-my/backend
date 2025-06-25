package org.ezcode.codetest.infrastructure.persistence.repository.community.discussion;

import java.util.List;

import org.ezcode.codetest.domain.community.dto.DiscussionQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiscussionQueryOptimizedRepository {

	Page<DiscussionQueryResult> findAllByProblemIdOptimized(Long problemId, String sortBy, Long currentUserId, Pageable pageable);

	List<Long> findDiscussionIdsByProblemId(Long problemId, String sortBy, Pageable pageable);

	List<DiscussionQueryResult> findDiscussionsByIds(List<Long> discussionIds, Long currentUserId);

}
