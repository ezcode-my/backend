package org.ezcode.codetest.application.community.service;

import org.ezcode.codetest.application.community.dto.response.VoteResponse;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.converter.NotificationConverter;
import org.ezcode.codetest.application.notification.dto.event.ReplyVoteEvent;
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
	private final NotificationConverter notificationConverter;

	public ReplyVoteService(
		ReplyVoteDomainService domainService,
		UserDomainService userDomainService,
		ReplyDomainService replyDomainService,
		NotificationEventService notificationEventService,
		NotificationConverter notificationConverter
	) {
		super(domainService, userDomainService);
		this.replyDomainService = replyDomainService;
		this.notificationEventService = notificationEventService;
		this.notificationConverter = notificationConverter;
	}

	@Transactional
	public VoteResponse toggleVoteOnReply(Long problemId, Long discussionId, Long replyId, Long userId) {

		Reply reply = voteDomainService.getValidatedReply(problemId, discussionId, replyId);

		return super.toggleVote(userId, reply.getId());
	}

	@Override
	protected void afterVote(User voter, Long targetId) {

		Reply reply = replyDomainService.getReplyById(targetId);
		if (voter.shouldSkipNotification(reply.getUser())) {
			return;
		}

		NotificationCreateEvent event = notificationConverter.convert(
			new ReplyVoteEvent(
				reply.getUser().getEmail(),
				reply.getId(),
				voter.getNickname()
			)
		);
		notificationEventService.saveAndNotify(event);
	}
}
