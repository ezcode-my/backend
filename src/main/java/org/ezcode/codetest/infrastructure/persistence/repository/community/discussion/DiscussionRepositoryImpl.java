package org.ezcode.codetest.infrastructure.persistence.repository.community.discussion;

import java.util.Optional;

import org.ezcode.codetest.domain.community.dto.DiscussionQueryResult;
import org.ezcode.codetest.domain.community.model.entity.Discussion;
import org.ezcode.codetest.domain.community.repository.DiscussionRepository;
import org.ezcode.codetest.domain.language.model.entity.Language;
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
	public Page<DiscussionQueryResult> findAllByProblemId(Long problemId, String sortBy, Long userId, Pageable pageable, Long ttt) {

		if (ttt == 1) {
			return discussionJpaRepository.findAllByProblemId(problemId, sortBy, userId, pageable);
		}
		return discussionJpaRepository.findAllByProblemIdOptimized(problemId, sortBy, userId, pageable);
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
