package org.ezcode.codetest.infrastructure.persistence.repository.community.reply;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.entity.Reply;
import org.ezcode.codetest.domain.community.repository.ReplyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepository {

	private final ReplyJpaRepository replyJpaRepository;

	@Override
	public Reply save(Reply reply) {

		return replyJpaRepository.save(reply);
	}

	@Override
	public Optional<Reply> findReplyById(Long replyId) {

		return replyJpaRepository.findById(replyId)
			.filter(r -> !r.isDeleted());
	}

	@Override
	public Page<Reply> findAllRepliesByDiscussionId(Long discussionId, Pageable pageable) {

		return replyJpaRepository.findAllByDiscussionId(discussionId, pageable);
	}

	@Override
	public Page<Reply> findAllChildRepliesByParentReplyId(Long parentReplyId, Pageable pageable) {

		return replyJpaRepository.findAllByParentReplyId(parentReplyId, pageable);
	}

	@Override
	public void updateReply(Reply reply, String content) {

		reply.update(content);
	}

	@Override
	public void deleteReply(Reply reply) {

		reply.setDeleted();
	}
}
