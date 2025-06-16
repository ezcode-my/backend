package org.ezcode.codetest.domain.community.service;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.VoteResult;
import org.ezcode.codetest.domain.community.model.entity.BaseVote;
import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.ezcode.codetest.domain.community.repository.BaseVoteRepository;
import org.ezcode.codetest.domain.user.model.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseVoteDomainService<T extends BaseVote, R extends BaseVoteRepository<T>> {

	protected final R voteRepository;

	public VoteResult manageVote(User voter, Long targetId, VoteType voteType) {

		Optional<T> existing = voteRepository.findByVoterIdAndTargetId(voter.getId(), targetId);

		if (existing.isPresent()) {
			// 추천 또는 비추천 기록이 존재
			T vote = existing.get();

			// 요청 타입이 NONE 이면 기존 기록 삭제
			if (voteType == VoteType.NONE) {
				voteRepository.delete(vote);
			} else {
				// 아닐 경우 타입 업데이트
				voteRepository.update(vote, voteType);
			}
		} else {
			// 기록이 없으면 새로 생성
			T vote = buildVote(voter, targetId, voteType);
			voteRepository.save(vote);
		}

		Long upvoteCount = voteRepository.countUpvotesByTargetId(targetId);
		Long downvoteCount = voteRepository.countDownvotesByTargetId(targetId);

		return new VoteResult(voteType, upvoteCount, downvoteCount);
	}

	public VoteResult getVoteStatus(Long voterId, Long targetId) {

		Optional<T> existing = voteRepository.findByVoterIdAndTargetId(voterId, targetId);

		VoteType voteType = existing.isPresent() ? existing.get().getVoteType() : VoteType.NONE;

		Long upvoteCount = voteRepository.countUpvotesByTargetId(targetId);
		Long downvoteCount = voteRepository.countDownvotesByTargetId(targetId);

		return new VoteResult(voteType, upvoteCount, downvoteCount);
	}

	protected abstract T buildVote(User voter, Long targetId, VoteType voteType);
}
