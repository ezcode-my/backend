package org.ezcode.codetest.domain.community.service;

import org.ezcode.codetest.domain.community.model.Reply;
import org.ezcode.codetest.domain.community.model.ReplyVote;
import org.ezcode.codetest.domain.community.repository.ReplyVoteRepository;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class ReplyVoteDomainService extends BaseVoteDomainService<ReplyVote, ReplyVoteRepository> {

	private final ReplyDomainService replyDomainService;

	public ReplyVoteDomainService(
		ReplyVoteRepository repository,
		ReplyDomainService replyDomainService
	) {
		super(repository);
		this.replyDomainService = replyDomainService;
	}

	@Override
	protected ReplyVote buildVote(User voter, Long targetId) {
		Reply reply = replyDomainService.getReplyById(targetId);

		return ReplyVote.builder()
			.voter(voter)
			.reply(reply)
			.build();
	}
}
