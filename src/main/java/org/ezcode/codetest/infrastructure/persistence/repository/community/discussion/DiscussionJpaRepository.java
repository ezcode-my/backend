package org.ezcode.codetest.infrastructure.persistence.repository.community.discussion;

import org.ezcode.codetest.domain.community.model.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiscussionJpaRepository extends JpaRepository<Discussion, Long> {

	@EntityGraph(attributePaths = { "user", "problem", "language" })
	@Query("""
		SELECT d
		FROM Discussion d
		WHERE d.problem.id = :problemId
		AND d.isDeleted = false
		ORDER BY d.createdAt DESC
		""")
	Page<Discussion> findAllByProblemId(Long problemId, Pageable pageable);

}
