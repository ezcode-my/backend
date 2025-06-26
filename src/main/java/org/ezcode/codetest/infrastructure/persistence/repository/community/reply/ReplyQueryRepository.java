package org.ezcode.codetest.infrastructure.persistence.repository.community.reply;

import org.ezcode.codetest.domain.community.dto.ReplyQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyQueryRepository {

	// 댓글 목록 조회
	Page<ReplyQueryResult> findRepliesByDiscussionId(Long discussionId, Long currentUserId, Pageable pageable);

	// 대댓글 목록 조회
	Page<ReplyQueryResult> findRepliesByParentId(Long parentId, Long currentUserId, Pageable pageable);

	Long countByDiscussionId(Long discussionId);

	Long countByParentId(Long parentId);

}
