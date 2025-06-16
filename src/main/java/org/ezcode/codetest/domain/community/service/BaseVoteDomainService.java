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
		VoteType prevVoteType = VoteType.NONE;

		if (voteType == VoteType.NONE) {
			existing.ifPresent(voteRepository::delete);
		} else {
			if (existing.isPresent()) {
				T vote = existing.get();

				prevVoteType = vote.getVoteType();
				voteRepository.update(vote, voteType);
			} else {
				T vote = buildVote(voter, targetId, voteType);

				voteRepository.save(vote);
			}
		}

		Long upvoteCount = voteRepository.countUpvotesByTargetId(targetId);
		Long downvoteCount = voteRepository.countDownvotesByTargetId(targetId);

		return new VoteResult(voteType, prevVoteType, upvoteCount, downvoteCount);
	}

	public VoteResult getVoteStatus(Long voterId, Long targetId) {

		Optional<T> existing = voteRepository.findByVoterIdAndTargetId(voterId, targetId);

		VoteType voteType = existing.isPresent() ? existing.get().getVoteType() : VoteType.NONE;

		Long upvoteCount = voteRepository.countUpvotesByTargetId(targetId);
		Long downvoteCount = voteRepository.countDownvotesByTargetId(targetId);

		return new VoteResult(voteType, VoteType.NONE, upvoteCount, downvoteCount);
	}

	protected abstract T buildVote(User voter, Long targetId, VoteType voteType);
}
