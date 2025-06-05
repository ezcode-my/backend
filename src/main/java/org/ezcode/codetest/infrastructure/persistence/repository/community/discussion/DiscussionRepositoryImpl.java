package org.ezcode.codetest.infrastructure.persistence.repository.community.discussion;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.community.repository.DiscussionRepository;
import org.ezcode.codetest.domain.problem.model.entity.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DiscussionRepositoryImpl implements DiscussionRepository {

	private final DiscussionJpaRepository discussionJpaRepository;

	@Override
	public Discussion save(Discussion discussion) {

		return discussionJpaRepository.save(discussion);
	}

	@Override
	public Optional<Discussion> findById(Long discussionId) {

		return discussionJpaRepository.findById(discussionId)
			.filter(d -> !d.isDeleted());
	}

	@Override
	public Page<Discussion> findAllByProblemId(Long problemId, Pageable pageable) {

		return discussionJpaRepository.findAllByProblemId(problemId, pageable);
	}

	@Override
	public void updateDiscussion(Discussion discussion, Language language, String content) {

		discussion.update(language, content);
	}

	@Override
	public void deleteDiscussion(Discussion discussion) {

		discussion.setDeleted();
	}
}
