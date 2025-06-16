package org.ezcode.codetest.domain.community.service;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.BaseVote;
import org.ezcode.codetest.domain.community.repository.BaseVoteRepository;
import org.ezcode.codetest.domain.user.model.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseVoteDomainService<T extends BaseVote, R extends BaseVoteRepository<T>> {

	protected final R voteRepository;

	/**
	 * 추천 도메인 서비스 공통 로직
	 * - 추천 또는 추천 취소 동작 수행
	 *
	 * @param voter 추천한 유저
	 * @param targetId 추천 대상 entity id (자유글, 댓글 등)
	 * @return 추천을 하면 true 반환, 취소하면 false 반환
	 */
	public boolean toggleVote(User voter, Long targetId) {

		Optional<T> existing = voteRepository.findByVoterIdAndTargetId(voter.getId(), targetId);

		if (existing.isPresent()) {
			voteRepository.delete(existing.get());
			return false;
		}

		T vote = buildVote(voter, targetId);
		voteRepository.save(vote);
		return true;
	}

	public boolean getVoteStatus(Long voterId, Long targetId) {

		return voteRepository.existsByVoterIdAndTargetId(voterId, targetId);
	}

	protected abstract T buildVote(User voter, Long targetId);
}
