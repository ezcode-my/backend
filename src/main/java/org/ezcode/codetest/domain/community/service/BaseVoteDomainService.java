package org.ezcode.codetest.domain.community.service;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.BaseVote;
import org.ezcode.codetest.domain.community.repository.BaseVoteRepository;
import org.ezcode.codetest.domain.user.model.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseVoteDomainService<T extends BaseVote, R extends BaseVoteRepository<T>> {

	protected final R repository;

	public boolean toggleVote(User voter, Long targetId) {

		Optional<T> existing = repository.findByVoterIdAndTargetId(voter.getId(), targetId);

		if (existing.isPresent()) {
			repository.delete(existing.get());
			return false;
		}

		T vote = buildVote(voter, targetId);
		repository.save(vote);
		return true;
	}

	public boolean getVoteStatus(Long voterId, Long targetId) {

		return repository.existsByVoterIdAndTargetId(voterId, targetId);
	}

	protected abstract T buildVote(User voter, Long targetId);
}
