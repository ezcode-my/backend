package org.ezcode.codetest.domain.community.service;

import org.ezcode.codetest.domain.community.model.DiscussionVote;
import org.ezcode.codetest.domain.community.repository.DiscussionVoteRepository;
import org.springframework.stereotype.Service;

@Service
public class DiscussionVoteDomainService extends BaseVoteDomainService<DiscussionVote, DiscussionVoteRepository> {

	public DiscussionVoteDomainService(DiscussionVoteRepository repository) {
		super(repository);
	}

}
