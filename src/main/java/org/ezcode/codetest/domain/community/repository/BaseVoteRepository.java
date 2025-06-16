package org.ezcode.codetest.domain.community.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.entity.BaseVote;

public interface BaseVoteRepository<T extends BaseVote> {

	T save(T voteEntity);

	// existBy~가 더 효율적이긴 하지만
	// 나중에 비추천 기능까지 생기면 재사용 가능
	Optional<T> findByVoterIdAndTargetId(Long voterId, Long targetId);

	boolean existsByVoterIdAndTargetId(Long voterId, Long targetId);

	void delete(T voteEntity);

}
