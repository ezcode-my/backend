package org.ezcode.codetest.domain.community.service;

import org.ezcode.codetest.domain.community.exception.CommunityException;
import org.ezcode.codetest.domain.community.exception.CommunityExceptionCode;
import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.community.model.Reply;
import org.ezcode.codetest.domain.community.repository.ReplyRepository;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyDomainService {

	private final ReplyRepository replyRepository;

	public Reply createReply(Discussion discussion, User user, Long parentReplyId, String content) {

		Reply parentReply = null;

		if (parentReplyId != null) {
			parentReply = getReplyById(parentReplyId);
			validateDiscussionMatches(parentReply, discussion);
		}

		Reply reply = Reply.builder()
			.discussion(discussion)
			.user(user)
			.parent(parentReply)
			.content(content)
			.build();

		return replyRepository.save(reply);
	}

	public Reply getReplyById(Long replyId) {

		return replyRepository.findReplyById(replyId)
			.orElseThrow(() -> new CommunityException(CommunityExceptionCode.REPLY_NOT_FOUND));
	}

	public Page<Reply> getRepliesByDiscussionId(Discussion discussion, Pageable pageable) {

		return replyRepository.findAllRepliesByDiscussionId(discussion.getId(), pageable);
	}

	public Page<Reply> getRepliesByParentReplyId(Long parentReplyId, Discussion discussion, Pageable pageable) {

		Reply parentReply = getReplyById(parentReplyId);
		validateDiscussionMatches(parentReply, discussion);

		return replyRepository.findAllChildRepliesByParentReplyId(parentReply.getId(), pageable);
	}

	public Reply modify(Long replyId, Discussion discussion, Long userId, String content) {

		Reply reply = getReplyById(replyId);

		validateDiscussionMatches(reply, discussion);
		validateIsAuthor(reply, userId);

		replyRepository.updateReply(reply, content);

		return reply;
	}

	public void remove(Long replyId, Discussion discussion, Long userId) {

		Reply reply = getReplyById(replyId);

		validateDiscussionMatches(reply, discussion);
		validateIsAuthor(reply, userId);

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
