package org.ezcode.codetest.infrastructure.persistence.repository.community.vote;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.entity.DiscussionVote;
import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.ezcode.codetest.domain.community.repository.DiscussionVoteRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DiscussionVoteRepositoryImpl implements DiscussionVoteRepository {

	private final DiscussionVoteJpaRepository repository;

	@Override
	public DiscussionVote save(DiscussionVote voteEntity) {

		return repository.save(voteEntity);
	}

	@Override
	public Optional<DiscussionVote> findByVoterIdAndTargetId(Long voterId, Long targetId) {

		return repository.findByVoterIdAndDiscussionId(voterId, targetId);
	}

	@Override
	public boolean existsByVoterIdAndTargetId(Long voterId, Long targetId) {

		return repository.existsByVoterIdAndDiscussionId(voterId, targetId);
	}

	@Override
	public void update(DiscussionVote vote, VoteType voteType) {

		vote.updateVoteType(voteType);
	}

	@Override
	public void delete(DiscussionVote voteEntity) {

		repository.delete(voteEntity);
	}

	@Override
	public Long countUpvotesByTargetId(Long targetId) {

		return repository.countUpvoteByDiscussionId(targetId, VoteType.UP);
	}

	@Override
	public Long countDownvotesByTargetId(Long targetId) {

		return repository.countDownvoteByDiscussionId(targetId, VoteType.DOWN);
	}
}
