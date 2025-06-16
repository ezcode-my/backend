package org.ezcode.codetest.application.community.service;

import java.util.Optional;

import org.ezcode.codetest.application.community.dto.response.VoteResponse;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.port.NotificationEventService;
import org.ezcode.codetest.domain.community.model.Reply;
import org.ezcode.codetest.domain.community.model.ReplyVote;
import org.ezcode.codetest.domain.community.service.ReplyDomainService;
import org.ezcode.codetest.domain.community.service.ReplyVoteDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReplyVoteService extends BaseVoteService<ReplyVote, ReplyVoteDomainService> {

	private final ReplyDomainService replyDomainService;

	private final NotificationEventService notificationEventService;

	public ReplyVoteService(
		ReplyVoteDomainService domainService,
		UserDomainService userDomainService,
		ReplyDomainService replyDomainService,
		NotificationEventService notificationEventService
	) {
		super(domainService, userDomainService);
		this.replyDomainService = replyDomainService;
		this.notificationEventService = notificationEventService;
	}

	@Transactional
	public VoteResponse toggleVoteOnReply(Long problemId, Long discussionId, Long replyId, Long userId) {

		Reply reply = voteDomainService.getValidatedReply(problemId, discussionId, replyId);

		return super.toggleVote(userId, reply.getId());
	}

	@Override
	protected void afterVote(User voter, Long targetId) {

		Reply reply = replyDomainService.getReplyById(targetId);

		Optional<NotificationCreateEvent> notificationEvent = voteDomainService.createReplyVoteNotification(voter, reply);

		notificationEvent.ifPresent(notificationEventService::saveAndNotify);
	}
}
