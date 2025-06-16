package org.ezcode.codetest.application.community.service;

import java.util.List;

import org.ezcode.codetest.application.community.dto.request.ReplyCreateRequest;
import org.ezcode.codetest.application.community.dto.request.ReplyModifyRequest;
import org.ezcode.codetest.application.community.dto.response.ReplyResponse;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.port.NotificationEventService;
import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.community.model.Reply;
import org.ezcode.codetest.domain.community.service.DiscussionDomainService;
import org.ezcode.codetest.domain.community.service.ReplyDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyService {

	private final ReplyDomainService replyDomainService;

	private final DiscussionDomainService discussionDomainService;
	private final UserDomainService userDomainService;

	private final NotificationEventService notificationEventService;

	@Transactional
	public ReplyResponse createReply(
		Long problemId,
		Long discussionId,
		ReplyCreateRequest request,
		Long userId
	) {

		User user = userDomainService.getUserById(userId);

		Discussion discussion = discussionDomainService.getDiscussionForProblem(discussionId, problemId);

		Reply reply = replyDomainService.createReply(discussion, user, request.parentReplyId(), request.content());

		List<User> notificationTargets = reply.generateNotificationTargets();

		if (!notificationTargets.isEmpty()) {
			for (User target : notificationTargets) {
				NotificationCreateEvent notificationEvent = replyDomainService.createReplyNotification(target, reply);
				notificationEventService.saveAndNotify(notificationEvent);
			}
		}

		return ReplyResponse.fromEntity(reply);
	}

	@Transactional(readOnly = true)
	public Page<ReplyResponse> getReplies(Long problemId, Long discussionId, Pageable pageable) {

		Discussion discussion = discussionDomainService.getDiscussionForProblem(discussionId, problemId);

		Page<Reply> replies = replyDomainService.getRepliesByDiscussionId(discussion, pageable);

		return replies.map(ReplyResponse::fromEntity);
	}

	@Transactional(readOnly = true)
	public Page<ReplyResponse> getChildReplies(
		Long problemId,
		Long discussionId,
		Long parentReplyId,
		Pageable pageable
	) {

		Discussion discussion = discussionDomainService.getDiscussionForProblem(discussionId, problemId);

		Page<Reply> replies = replyDomainService.getRepliesByParentReplyId(parentReplyId, discussion, pageable);

		return replies.map(ReplyResponse::fromEntity);
	}

	@Transactional
	public ReplyResponse modifyReply(
		Long problemId,
		Long discussionId,
		Long replyId,
		ReplyModifyRequest request,
		Long userId
	) {

		Discussion discussion = discussionDomainService.getDiscussionForProblem(discussionId, problemId);

		Reply reply = replyDomainService.modify(replyId, discussion, userId, request.content());

		return ReplyResponse.fromEntity(reply);
	}

	// TODO: 댓글 삭제 시 대댓글들을 어떻게 처리할지 추후 의논 필요
	@Transactional
	public void removeReply(Long problemId, Long discussionId, Long replyId, Long userId) {

		Discussion discussion = discussionDomainService.getDiscussionForProblem(discussionId, problemId);

		replyDomainService.remove(replyId, discussion, userId);
	}
}
