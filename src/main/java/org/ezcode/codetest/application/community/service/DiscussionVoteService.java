package org.ezcode.codetest.application.community.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.application.community.dto.request.VoteRequest;
import org.ezcode.codetest.application.community.dto.response.VoteResponse;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.service.NotificationExecutor;
import org.ezcode.codetest.domain.community.model.entity.Discussion;
import org.ezcode.codetest.domain.community.model.entity.DiscussionVote;
import org.ezcode.codetest.domain.community.service.DiscussionDomainService;
import org.ezcode.codetest.domain.community.service.DiscussionVoteDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DiscussionVoteService extends BaseVoteService<DiscussionVote, DiscussionVoteDomainService> {

	private final DiscussionDomainService discussionDomainService;
	private final NotificationExecutor notificationExecutor;

	public DiscussionVoteService(
		DiscussionVoteDomainService domainService,
		UserDomainService userDomainService,
		DiscussionDomainService discussionDomainService,
		NotificationExecutor notificationExecutor
	) {
		super(domainService, userDomainService);
		this.discussionDomainService = discussionDomainService;
		this.notificationExecutor = notificationExecutor;
	}

	@Transactional
	public VoteResponse manageVoteOnDiscussion(Long problemId, Long discussionId, VoteRequest request, Long userId) {

		Discussion discussion = voteDomainService.getValidatedDiscussion(discussionId, problemId);

		return super.manageVote(userId, discussion.getId(), request.voteType());
	}

	@Override
	protected void afterVote(User voter, Long targetId) {

		notificationExecutor.execute(() -> {

			try {
				Discussion discussion = discussionDomainService.getDiscussionById(targetId);

				Optional<NotificationCreateEvent> notificationEvent = voteDomainService.createDiscussionVoteNotification(voter, discussion);

				return notificationEvent.map(List::of).orElse(Collections.emptyList());
			} catch (Exception ex) {
				log.error("토론글 추천 알림 생성 중 에러 발생 : {}", ex.getMessage());
				return Collections.emptyList();
			}
		});
	}
}
