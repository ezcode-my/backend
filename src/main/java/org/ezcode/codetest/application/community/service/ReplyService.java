package org.ezcode.codetest.application.community.service;

import java.util.Collections;
import java.util.List;

import org.ezcode.codetest.application.community.dto.request.ReplyCreateRequest;
import org.ezcode.codetest.application.community.dto.request.ReplyModifyRequest;
import org.ezcode.codetest.application.community.dto.response.ReplyResponse;
import org.ezcode.codetest.application.notification.service.NotificationExecutor;
import org.ezcode.codetest.domain.community.dto.ReplyQueryResult;
import org.ezcode.codetest.domain.community.model.entity.Discussion;
import org.ezcode.codetest.domain.community.model.entity.Reply;
import org.ezcode.codetest.domain.community.service.DiscussionDomainService;
import org.ezcode.codetest.domain.community.service.ReplyDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyService {

	private final ReplyDomainService replyDomainService;

	private final DiscussionDomainService discussionDomainService;
	private final UserDomainService userDomainService;

	private final NotificationExecutor notificationExecutor;

	@Transactional
	public ReplyResponse createReply(
		Long problemId,
		Long discussionId,
		ReplyCreateRequest request,
		Long userId
	) {

		User user = userDomainService.getUserById(userId);

		Discussion discussion = discussionDomainService.getDiscussionForProblem(discussionId, problemId);

		Reply reply = replyDomainService.createReply(discussion, user, request.parentReplyId(), request.content());

		notificationExecutor.execute(() -> {
			try {
				List<User> notificationTargets = reply.generateNotificationTargets();

				if (notificationTargets.isEmpty()) {
					return Collections.emptyList();
				}

				return notificationTargets.stream()
					.map(target -> replyDomainService.createReplyNotification(target, reply))
					.toList();
			} catch (Exception ex) {
				log.error("댓글 알림 생성 중 에러 발생 : {}", ex.getMessage());
				return Collections.emptyList();
			}
		});

		return ReplyResponse.fromEntity(reply);
	}

	@Transactional(readOnly = true)
	public Page<ReplyResponse> getReplies(Long problemId, Long discussionId, Long currentUserId, Pageable pageable) {

		Discussion discussion = discussionDomainService.getDiscussionForProblem(discussionId, problemId);

		Page<ReplyQueryResult> replies = replyDomainService.getRepliesByDiscussionId(discussion, currentUserId, pageable);

		return replies.map(ReplyResponse::from);
	}

	@Transactional(readOnly = true)
	public Page<ReplyResponse> getChildReplies(
		Long problemId,
		Long discussionId,
		Long parentReplyId,
		Long currentUserId,
		Pageable pageable
	) {

		Discussion discussion = discussionDomainService.getDiscussionForProblem(discussionId, problemId);

		Page<ReplyQueryResult> childReplies =
			replyDomainService.getRepliesByParentReplyId(parentReplyId, discussion, currentUserId, pageable);

		return childReplies.map(ReplyResponse::from);
	}

	@Transactional
	public ReplyResponse modifyReply(
		Long problemId,
		Long discussionId,
		Long replyId,
		ReplyModifyRequest request,
		Long userId
	) {

		Discussion discussion = discussionDomainService.getDiscussionForProblem(discussionId, problemId);

		Reply reply = replyDomainService.modify(replyId, discussion, userId, request.content());

		return ReplyResponse.fromEntity(reply);
	}

	// TODO: 댓글 삭제 시 대댓글들을 어떻게 처리할지 추후 의논 필요
	@Transactional
	public void removeReply(Long problemId, Long discussionId, Long replyId, Long userId) {

		Discussion discussion = discussionDomainService.getDiscussionForProblem(discussionId, problemId);

		replyDomainService.remove(replyId, discussion, userId);
	}
}
