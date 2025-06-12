package org.ezcode.codetest.application.community.service;

import org.ezcode.codetest.application.community.dto.response.VoteResponse;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.converter.NotificationConverter;
import org.ezcode.codetest.application.notification.dto.event.DiscussionVoteEvent;
import org.ezcode.codetest.application.notification.port.NotificationEventService;
import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.community.model.DiscussionVote;
import org.ezcode.codetest.domain.community.service.DiscussionDomainService;
import org.ezcode.codetest.domain.community.service.DiscussionVoteDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiscussionVoteService extends BaseVoteService<DiscussionVote, DiscussionVoteDomainService> {

	private final UserDomainService userDomainService;
	private final DiscussionDomainService discussionDomainService;

	private final NotificationEventService notificationEventService;
	private final NotificationConverter notificationConverter;

	public DiscussionVoteService(
		DiscussionVoteDomainService domainService,
		UserDomainService userDomainService,
		DiscussionDomainService discussionDomainService,
		NotificationEventService notificationEventService,
		NotificationConverter notificationConverter
	) {
		super(domainService);
		this.userDomainService = userDomainService;
		this.discussionDomainService = discussionDomainService;
		this.notificationEventService = notificationEventService;
		this.notificationConverter = notificationConverter;
	}

	@Transactional
	public VoteResponse validateAndToggleVote(Long problemId, Long discussionId, Long userId) {

		User voter = userDomainService.getUserById(userId);

		Discussion discussion = discussionDomainService.getAndValidateDiscussionForProblem(discussionId, problemId);

		return toggleVote(voter, discussion.getId());
	}

	@Override
	protected void afterVote(User voter, Long targetId) {

		Discussion discussion = discussionDomainService.getDiscussionById(targetId);
		if (voter.shouldSkipNotification(discussion.getUser())) {
			return;
		}

		NotificationCreateEvent event = notificationConverter.convert(
			new DiscussionVoteEvent(
				discussion.getUser().getEmail(),
				discussion.getId(),
				voter.getNickname()
			)
		);
		notificationEventService.saveAndNotify(event);
	}
}
