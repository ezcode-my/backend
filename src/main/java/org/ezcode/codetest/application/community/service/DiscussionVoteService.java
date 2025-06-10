package org.ezcode.codetest.application.community.service;

import java.util.Optional;

import org.ezcode.codetest.application.community.dto.response.VoteResponse;
import org.ezcode.codetest.application.notification.port.NotificationEventDtoFactory;
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

	public DiscussionVoteService(
		DiscussionVoteDomainService domainService,
		UserDomainService userDomainService,
		DiscussionDomainService discussionDomainService,
		NotificationEventService notificationEventService
	) {
		super(domainService);
		this.userDomainService = userDomainService;
		this.discussionDomainService = discussionDomainService;
		this.notificationEventService = notificationEventService;
	}

	@Transactional
	public VoteResponse validateAndToggleVote(Long problemId, Long discussionId, Long userId) {

		User voter = userDomainService.getUserById(userId);

		// validate
		Discussion discussion = discussionDomainService.getDiscussionById(discussionId);
		discussionDomainService.validateProblemMatches(discussion, problemId);

		Optional<DiscussionVote> discussionVote = toggleVote(voter, discussionId);
		return new VoteResponse(discussionVote.isPresent());
	}

	@Override
	protected DiscussionVote buildVoteEntity(User voter, Long targetId) {
		Discussion discussion = discussionDomainService.getDiscussionById(targetId);

		return DiscussionVote.builder()
			.voter(voter)
			.discussion(discussion)
			.build();
	}

	@Override
	protected void afterVote(User voter, Long targetId) {

		Discussion discussion = discussionDomainService.getDiscussionById(targetId);
		if (voter.shouldSkipNotification(discussion.getUser())) {
			return;
		}
	}
}
