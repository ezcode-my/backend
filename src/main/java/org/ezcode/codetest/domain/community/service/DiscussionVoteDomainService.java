package org.ezcode.codetest.domain.community.service;

import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.community.model.DiscussionVote;
import org.ezcode.codetest.domain.community.repository.DiscussionVoteRepository;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class DiscussionVoteDomainService extends BaseVoteDomainService<DiscussionVote, DiscussionVoteRepository> {

	private final DiscussionDomainService discussionDomainService;

	public DiscussionVoteDomainService(
		DiscussionVoteRepository repository,
		DiscussionDomainService discussionDomainService
	) {
		super(repository);
		this.discussionDomainService = discussionDomainService;
	}

	@Override
	protected DiscussionVote buildVote(User voter, Long targetId) {
		Discussion discussion = discussionDomainService.getDiscussionById(targetId);

		return DiscussionVote.builder()
			.voter(voter)
			.discussion(discussion)
			.build();
	}
}
