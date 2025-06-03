package org.ezcode.codetest.domain.community.service;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.BaseVote;
import org.ezcode.codetest.domain.community.repository.BaseVoteRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseVoteDomainService<T extends BaseVote, R extends BaseVoteRepository<T>> {

	protected final R repository;

	public T createVoteEntity(T entity) {

		return repository.save(entity);
	}

	public Optional<T> getVoteEntity(Long voterId, Long targetId) {

		return repository.findByVoterIdAndTargetId(voterId, targetId);
	}

	public boolean getVoteStatus(Long voterId, Long targetId) {

		return repository.existsByVoterIdAndTargetId(voterId, targetId);
	}

	public void removeVoteEntity(T entity) {

		repository.delete(entity);
	}
}
