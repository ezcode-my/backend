package org.ezcode.codetest.infrastructure.persistence.repository.community;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiscussionJpaRepository extends JpaRepository<Discussion, Long> {

	@Query("SELECT d FROM Discussion d WHERE d.isDeleted = false")
	Optional<Discussion> findByDiscussionId(Long discussionId);

	@EntityGraph(attributePaths = { "user", "problem", "language" })
	@Query("""
		SELECT d
		FROM Discussion d
		WHERE d.problem.id = :problemId
		AND d.isDeleted = false
		ORDER BY d.createdAt DESC
		""")
	Page<Discussion> findAllByProblem(Long problemId, Pageable pageable);

}
