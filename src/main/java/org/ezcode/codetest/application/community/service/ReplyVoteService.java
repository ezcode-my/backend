package org.ezcode.codetest.application.community.service;

import java.util.Optional;

import org.ezcode.codetest.application.community.dto.response.VoteResponse;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.dto.converter.NotificationConverter;
import org.ezcode.codetest.application.notification.dto.event.ReplyVoteEvent;
import org.ezcode.codetest.application.notification.port.NotificationEventService;
import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.community.model.Reply;
import org.ezcode.codetest.domain.community.model.ReplyVote;
import org.ezcode.codetest.domain.community.service.DiscussionDomainService;
import org.ezcode.codetest.domain.community.service.ReplyDomainService;
import org.ezcode.codetest.domain.community.service.ReplyVoteDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReplyVoteService extends BaseVoteService<ReplyVote, ReplyVoteDomainService> {

	private final UserDomainService userDomainService;
	private final ReplyDomainService replyDomainService;
	private final DiscussionDomainService discussionDomainService;

	private final NotificationEventService notificationEventService;
	private final NotificationConverter notificationConverter;

	public ReplyVoteService(
		ReplyVoteDomainService domainService,
		UserDomainService userDomainService,
		ReplyDomainService replyDomainService,
		DiscussionDomainService discussionDomainService,
		NotificationEventService notificationEventService,
		NotificationConverter notificationConverter
	) {
		super(domainService);
		this.userDomainService = userDomainService;
		this.replyDomainService = replyDomainService;
		this.discussionDomainService = discussionDomainService;
		this.notificationEventService = notificationEventService;
		this.notificationConverter = notificationConverter;
	}

	@Transactional
	public VoteResponse validateAndToggleVote(Long problemId, Long discussionId, Long replyId, Long userId) {

		User voter = userDomainService.getUserById(userId);

		// validate
		Discussion discussion = discussionDomainService.getDiscussionById(discussionId);
		discussionDomainService.validateProblemMatches(discussion, problemId);
		Reply reply = replyDomainService.getReplyById(replyId);
		replyDomainService.validateDiscussionMatches(reply, discussion);

		Optional<ReplyVote> replyVote = toggleVote(voter, replyId);
		return new VoteResponse(replyVote.isPresent());
	}

	@Override
	protected ReplyVote buildVoteEntity(User voter, Long targetId) {
		Reply reply = replyDomainService.getReplyById(targetId);

		return ReplyVote.builder()
			.voter(voter)
			.reply(reply)
			.build();
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
