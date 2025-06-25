package org.ezcode.codetest.infrastructure.persistence.repository.community.discussion;

import org.ezcode.codetest.domain.community.model.entity.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionJpaRepository extends JpaRepository<Discussion, Long>, DiscussionQueryRepository, DiscussionQueryOptimizedRepository {
}
