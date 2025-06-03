package org.ezcode.codetest.infrastructure.persistence.repository.community.vote;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.ReplyVote;
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
	public void delete(ReplyVote voteEntity) {

		repository.delete(voteEntity);
	}
}
