package org.ezcode.codetest.infrastructure.persistence.repository.community.vote;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.entity.ReplyVote;
import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.ezcode.codetest.domain.community.repository.ReplyVoteRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReplyVoteRepositoryImpl implements ReplyVoteRepository {

	private final ReplyVoteJpaRepository repository;

	@Override
	public ReplyVote save(ReplyVote voteEntity) {

		return repository.save(voteEntity);
	}

	@Override
	public Optional<ReplyVote> findByVoterIdAndTargetId(Long voterId, Long targetId) {

		return repository.findByVoterIdAndReplyId(voterId, targetId);
	}

	@Override
	public boolean existsByVoterIdAndTargetId(Long voterId, Long targetId) {

		return repository.existsByVoterIdAndReplyId(voterId, targetId);
	}

	@Override
	public void update(ReplyVote vote, VoteType voteType) {

		vote.updateVoteType(voteType);
	}

	@Override
	public void delete(ReplyVote voteEntity) {

		repository.delete(voteEntity);
	}

	@Override
	public Long countUpvotesByTargetId(Long targetId) {

		return repository.countUpvoteByReplyId(targetId, VoteType.UP);
	}

	@Override
	public Long countDownvotesByTargetId(Long targetId) {

		return repository.countDownvoteByReplyId(targetId, VoteType.DOWN);
	}
}
