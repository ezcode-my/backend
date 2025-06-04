package org.ezcode.codetest.infrastructure.persistence.repository.community.reply;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long> {

	@Query("""
		SELECT r
		FROM Reply r
		WHERE r.id = :replyId
		AND r.isDeleted = false
		""")
	Optional<Reply> findByReplyId(Long replyId);

	@EntityGraph(attributePaths = { "user" })
	@Query("""
		SELECT r
		FROM Reply r
		WHERE r.discussion.id = :discussionId
		AND r.isDeleted = false
		AND r.parent IS NULL
		ORDER BY r.createdAt DESC
		""")
	Page<Reply> findAllByDiscussionId(Long discussionId, Pageable pageable);

	@EntityGraph(attributePaths = { "user" })
	@Query("""
		SELECT r
		FROM Reply r
		WHERE r.parent.id = :parentReplyId
		AND r.isDeleted = false
		ORDER BY r.createdAt ASC
		""")
	Page<Reply> findAllByParentReplyId(Long parentReplyId, Pageable pageable);

}
