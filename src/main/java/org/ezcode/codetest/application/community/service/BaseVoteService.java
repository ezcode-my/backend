package org.ezcode.codetest.application.community.service;

import java.util.Optional;

import org.ezcode.codetest.application.community.dto.response.VoteResponse;
import org.ezcode.codetest.domain.community.model.BaseVote;
import org.ezcode.codetest.domain.community.service.BaseVoteDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseVoteService<T extends BaseVote, D extends BaseVoteDomainService<T, ?>> {

	protected final D domainService;

	protected Optional<T> toggleVote(User voter, Long targetId) {

		Optional<T> existing = domainService.getVoteEntity(voter.getId(), targetId);
		if (existing.isPresent()) {
			domainService.removeVoteEntity(existing.get());
			return Optional.empty();
		} else {
			T voteEntity = buildVoteEntity(voter, targetId);
			afterVote(voter, targetId);
			return Optional.of(domainService.createVoteEntity(voteEntity));
		}
	}

	@Transactional(readOnly = true)
	public VoteResponse getVoteStatus(Long userId, Long targetId) {

		boolean voteStatus = domainService.getVoteStatus(userId, targetId);
		return new VoteResponse(voteStatus);
	}

	protected abstract T buildVoteEntity(User voter, Long targetId);

	protected abstract void afterVote(User voter, Long targetId);

}
