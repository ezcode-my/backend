package org.ezcode.codetest.application.community.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.application.community.dto.request.VoteRequest;
import org.ezcode.codetest.application.community.dto.response.VoteResponse;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.service.NotificationExecutor;
import org.ezcode.codetest.domain.community.model.entity.Reply;
import org.ezcode.codetest.domain.community.model.entity.ReplyVote;
import org.ezcode.codetest.domain.community.service.ReplyDomainService;
import org.ezcode.codetest.domain.community.service.ReplyVoteDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReplyVoteService extends BaseVoteService<ReplyVote, ReplyVoteDomainService> {

	private final ReplyDomainService replyDomainService;
	private final NotificationExecutor notificationExecutor;


	public ReplyVoteService(
		ReplyVoteDomainService domainService,
		UserDomainService userDomainService,
		ReplyDomainService replyDomainService,
		NotificationExecutor notificationExecutor
	) {
		super(domainService, userDomainService);
		this.replyDomainService = replyDomainService;
		this.notificationExecutor = notificationExecutor;
	}

	@Transactional
	public VoteResponse manageVoteOnReply(Long problemId, Long discussionId, Long replyId, VoteRequest request, Long userId) {

		Reply reply = voteDomainService.getValidatedReply(problemId, discussionId, replyId);

		return super.manageVote(userId, reply.getId(), request.voteType());
	}

	@Override
	protected void afterVote(User voter, Long targetId) {

		notificationExecutor.execute(() -> {
			Reply reply = replyDomainService.getReplyById(targetId);

			Optional<NotificationCreateEvent> notificationEvent = voteDomainService.createReplyVoteNotification(voter,
				reply);

			return notificationEvent.map(List::of).orElse(Collections.emptyList());
		});
	}
}
