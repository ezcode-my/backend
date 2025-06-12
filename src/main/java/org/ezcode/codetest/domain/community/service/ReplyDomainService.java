package org.ezcode.codetest.domain.community.service;

import org.ezcode.codetest.domain.community.exception.CommunityException;
import org.ezcode.codetest.domain.community.exception.CommunityExceptionCode;
import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.community.model.Reply;
import org.ezcode.codetest.domain.community.repository.ReplyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyDomainService {

	private final ReplyRepository replyRepository;

	public Reply createReply(Reply reply) {

		return replyRepository.save(reply);
	}

	public Reply getReplyById(Long replyId) {

		return replyRepository.findReplyById(replyId)
			.orElseThrow(() -> new CommunityException(CommunityExceptionCode.REPLY_NOT_FOUND));
	}

	public Page<Reply> getRepliesByDiscussionId(Discussion discussion, Pageable pageable) {

		return replyRepository.findAllRepliesByDiscussionId(discussion.getId(), pageable);
	}

	public Page<Reply> getRepliesByParentReplyId(Long parentReplyId, Pageable pageable) {

		return replyRepository.findAllChildRepliesByParentReplyId(parentReplyId, pageable);
	}

	public void modify(Reply reply, String content) {

		replyRepository.updateReply(reply, content);
	}

	public void remove(Reply reply) {

		replyRepository.deleteReply(reply);
	}


	public void validateDiscussionMatches(Reply reply, Discussion discussion) {

		if (!reply.isDiscussionMatches(discussion.getId())) {
			throw new CommunityException(CommunityExceptionCode.REPLY_DISCUSSION_MISMATCH);
		}
	}

	public void validateIsAuthor(Reply reply, Long userId) {

		if (!reply.isAuthor(userId)) {
			throw new CommunityException(CommunityExceptionCode.USER_NOT_AUTHOR);
		}
	}
}
