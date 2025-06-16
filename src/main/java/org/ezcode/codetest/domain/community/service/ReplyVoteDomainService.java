package org.ezcode.codetest.domain.community.service;

import java.util.Optional;

import org.ezcode.codetest.application.notification.enums.NotificationType;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.payload.ReplyVotePayload;
import org.ezcode.codetest.domain.community.model.entity.Discussion;
import org.ezcode.codetest.domain.community.model.entity.Reply;
import org.ezcode.codetest.domain.community.model.entity.ReplyVote;
import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.ezcode.codetest.domain.community.repository.ReplyVoteRepository;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class ReplyVoteDomainService extends BaseVoteDomainService<ReplyVote, ReplyVoteRepository> {

	private final ReplyDomainService replyDomainService;
	private final DiscussionDomainService discussionDomainService;

	public ReplyVoteDomainService(
		ReplyVoteRepository repository,
		ReplyDomainService replyDomainService,
		DiscussionDomainService discussionDomainService
	) {

		super(repository);
		this.replyDomainService = replyDomainService;
		this.discussionDomainService = discussionDomainService;
	}

	@Override
	protected ReplyVote buildVote(User voter, Long targetId, VoteType voteType) {

		Reply reply = replyDomainService.getReplyById(targetId);

		return ReplyVote.builder()
			.voter(voter)
			.reply(reply)
			.voteType(voteType)
			.build();
	}

	public Reply getValidatedReply(Long problemId, Long discussionId, Long replyId) {

		Discussion discussion = discussionDomainService.getDiscussionById(discussionId);
		discussionDomainService.validateProblemMatches(discussion, problemId);

		Reply reply = replyDomainService.getReplyById(replyId);
		replyDomainService.validateDiscussionMatches(reply, discussion);

		return reply;
	}

	public Optional<NotificationCreateEvent> createReplyVoteNotification(User voter, Reply reply) {

		if (voter.shouldSkipNotification(reply.getUser())) {
			return Optional.empty();
		}

		ReplyVotePayload payload = new ReplyVotePayload(
			reply.getDiscussion().getProblemId(),
			reply.getId(),
			voter.getNickname()
		);

		return Optional.of(
			NotificationCreateEvent.of(
				reply.getUserEmail(),
				NotificationType.COMMUNITY_REPLY_VOTED_UP,
				payload
			)
		);
	}
}
