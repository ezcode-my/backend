package org.ezcode.codetest.domain.community.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.community.dto.ReplyQueryResult;
import org.ezcode.codetest.domain.community.model.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyRepository {

	Reply save(Reply reply);

	Optional<Reply> findReplyById(Long replyId);

	Page<ReplyQueryResult> findAllRepliesByDiscussionId(Long discussionId, Long currentUserId, Pageable pageable);

	Page<ReplyQueryResult> findAllChildRepliesByParentReplyId(Long parentReplyId, Long currentUserId, Pageable pageable);

	void updateReply(Reply reply, String content);

	void deleteReply(Reply reply);

}
