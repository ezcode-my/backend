package org.ezcode.codetest.domain.community.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyRepository {

	Reply save(Reply reply);

	Optional<Reply> findReplyById(Long replyId);

	Page<Reply> findAllRepliesByDiscussionId(Long discussionId, Pageable pageable);

	Page<Reply> findAllChildRepliesByParentReplyId(Long parentReplyId, Pageable pageable);

	void updateReply(Reply reply, String content);

	void deleteReply(Reply reply);

}
