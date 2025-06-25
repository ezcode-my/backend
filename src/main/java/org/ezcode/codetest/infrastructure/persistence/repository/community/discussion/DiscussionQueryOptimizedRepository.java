package org.ezcode.codetest.infrastructure.persistence.repository.community.discussion;

import org.ezcode.codetest.domain.community.dto.DiscussionQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiscussionQueryOptimizedRepository {

	Page<DiscussionQueryResult> findAllByProblemIdOptimized(Long problemId, String sortBy, Long currentUserId, Pageable pageable);

}
