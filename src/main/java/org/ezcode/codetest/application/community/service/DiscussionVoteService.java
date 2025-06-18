package org.ezcode.codetest.application.community.service;

import java.util.Optional;

import org.ezcode.codetest.application.community.dto.request.VoteRequest;
import org.ezcode.codetest.application.community.dto.response.VoteResponse;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.port.NotificationEventService;
import org.ezcode.codetest.domain.community.model.entity.Discussion;
import org.ezcode.codetest.domain.community.model.entity.DiscussionVote;
import org.ezcode.codetest.domain.community.service.DiscussionDomainService;
import org.ezcode.codetest.domain.community.service.DiscussionVoteDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiscussionVoteService extends BaseVoteService<DiscussionVote, DiscussionVoteDomainService> {

	private final DiscussionDomainService discussionDomainService;
	private final NotificationEventService notificationEventService;

	public DiscussionVoteService(
		DiscussionVoteDomainService domainService,
		UserDomainService userDomainService,
		DiscussionDomainService discussionDomainService,
		NotificationEventService notificationEventService
	) {
		super(domainService, userDomainService);
		this.discussionDomainService = discussionDomainService;
		this.notificationEventService = notificationEventService;
	}

	@Transactional
	public VoteResponse manageVoteOnDiscussion(Long problemId, Long discussionId, VoteRequest request, Long userId) {

		Discussion discussion = voteDomainService.getValidatedDiscussion(discussionId, problemId);

		return super.manageVote(userId, discussion.getId(), request.voteType());
	}

	@Override
	protected void afterVote(User voter, Long targetId) {

		Discussion discussion = discussionDomainService.getDiscussionById(targetId);

		Optional<NotificationCreateEvent> notificationEvent = voteDomainService.createDiscussionVoteNotification(voter, discussion);

		notificationEvent.ifPresent(notificationEventService::saveAndNotify);
	}
}
