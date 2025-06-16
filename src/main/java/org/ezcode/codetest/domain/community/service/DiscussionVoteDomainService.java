package org.ezcode.codetest.domain.community.service;

import java.util.Optional;

import org.ezcode.codetest.application.notification.enums.NotificationType;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.payload.DiscussionVotePayload;
import org.ezcode.codetest.domain.community.model.entity.Discussion;
import org.ezcode.codetest.domain.community.model.entity.DiscussionVote;
import org.ezcode.codetest.domain.community.repository.DiscussionVoteRepository;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class DiscussionVoteDomainService extends BaseVoteDomainService<DiscussionVote, DiscussionVoteRepository> {

	private final DiscussionDomainService discussionDomainService;

	public DiscussionVoteDomainService(
		DiscussionVoteRepository repository,
		DiscussionDomainService discussionDomainService
	) {

		super(repository);
		this.discussionDomainService = discussionDomainService;
	}

	@Override
	protected DiscussionVote buildVote(User voter, Long targetId) {

		Discussion discussion = discussionDomainService.getDiscussionById(targetId);

		return DiscussionVote.builder()
			.voter(voter)
			.discussion(discussion)
			.build();
	}

	public Discussion getValidatedDiscussion(Long discussionId, Long problemId) {

		Discussion discussion = discussionDomainService.getDiscussionById(discussionId);
		discussionDomainService.validateProblemMatches(discussion, problemId);

		return discussion;
	}

	public Optional<NotificationCreateEvent> createDiscussionVoteNotification(User voter, Discussion discussion) {

		if (voter.shouldSkipNotification(discussion.getUser())) {
			return Optional.empty();
		}

		DiscussionVotePayload payload = new DiscussionVotePayload(
			discussion.getProblemId(),
			discussion.getId(),
			voter.getNickname()
		);

		return Optional.of(
			NotificationCreateEvent.of(
				discussion.getUserEmail(),
				NotificationType.COMMUNITY_DISCUSSION_VOTED_UP,
				payload
			)
		);
	}
}
