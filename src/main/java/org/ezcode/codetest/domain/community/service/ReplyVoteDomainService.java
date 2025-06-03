package org.ezcode.codetest.domain.community.service;

import org.ezcode.codetest.domain.community.model.ReplyVote;
import org.ezcode.codetest.domain.community.repository.ReplyVoteRepository;
import org.springframework.stereotype.Service;

@Service
public class ReplyVoteDomainService extends BaseVoteDomainService<ReplyVote, ReplyVoteRepository> {

	public ReplyVoteDomainService(ReplyVoteRepository repository) {
		super(repository);
	}
}
