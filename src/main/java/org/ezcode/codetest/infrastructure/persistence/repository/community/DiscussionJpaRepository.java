package org.ezcode.codetest.infrastructure.persistence.repository.community;

import org.ezcode.codetest.domain.community.model.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionJpaRepository extends JpaRepository<Discussion, Long> {
}
