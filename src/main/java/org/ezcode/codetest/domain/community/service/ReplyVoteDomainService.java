package org.ezcode.codetest.domain.community.service;

import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.community.model.Reply;
import org.ezcode.codetest.domain.community.model.ReplyVote;
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
	protected ReplyVote buildVote(User voter, Long targetId) {

		Reply reply = replyDomainService.getReplyById(targetId);

		return ReplyVote.builder()
			.voter(voter)
			.reply(reply)
			.build();
	}

	public Reply getValidatedReply(Long problemId, Long discussionId, Long replyId) {

		Discussion discussion = discussionDomainService.getDiscussionById(discussionId);
		discussionDomainService.validateProblemMatches(discussion, problemId);

		Reply reply = replyDomainService.getReplyById(replyId);
		replyDomainService.validateDiscussionMatches(reply, discussion);

		return reply;
	}
}
