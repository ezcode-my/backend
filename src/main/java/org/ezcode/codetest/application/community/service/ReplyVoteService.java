package org.ezcode.codetest.application.community.service;

import java.util.Optional;

import org.ezcode.codetest.application.community.dto.response.VoteResponse;
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

	public ReplyVoteService(
		ReplyVoteDomainService domainService,
		UserDomainService userDomainService,
		ReplyDomainService replyDomainService,
		DiscussionDomainService discussionDomainService
	) {
		super(domainService);
		this.userDomainService = userDomainService;
		this.replyDomainService = replyDomainService;
		this.discussionDomainService = discussionDomainService;
	}

	@Override
	protected ReplyVote buildVoteEntity(User voter, Long targetId) {
		Reply reply = replyDomainService.getReplyById(targetId);

		return ReplyVote.builder()
			.voter(voter)
			.reply(reply)
			.build();
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
}
