package org.ezcode.codetest.domain.community.service;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.VoteResult;
import org.ezcode.codetest.domain.community.model.entity.BaseVote;
import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.ezcode.codetest.domain.community.repository.BaseVoteRepository;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.dao.DataIntegrityViolationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

				try {
					voteRepository.save(vote);
				} catch (DataIntegrityViolationException ex) {
					// 중복 삽입 예외는 “이미 UP 상태”로 간주하고 흐름을 계속
					// 만약 이 로그가 빈번하게 발생한다면 어떤 이유로 연속 요청(따닥 문제)가 발생하는지 확인해봐야 함
					log.info("중복 추천 시도 감지 (따닥 문제): voter={} target={}", voter.getId(), targetId);
				}
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
