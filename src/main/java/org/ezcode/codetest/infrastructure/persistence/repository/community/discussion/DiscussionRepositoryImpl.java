package org.ezcode.codetest.infrastructure.persistence.repository.community.discussion;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.ezcode.codetest.domain.community.dto.DiscussionQueryResult;
import org.ezcode.codetest.domain.community.model.entity.Discussion;
import org.ezcode.codetest.domain.community.repository.DiscussionRepository;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
		} else if (ttt == 2) {
			return discussionJpaRepository.findAllByProblemIdOptimized(problemId, sortBy, userId, pageable);
		} else if (ttt == 3) {
			List<Long> ids = discussionJpaRepository.findDiscussionIdsByProblemId(problemId, sortBy, pageable);
			List<DiscussionQueryResult> results = discussionJpaRepository.findDiscussionsByIds(ids, userId);

			long totalCount = 10; //discussionQueryRepository.countByProblemId(problemId);

			// 4. (매우 중요) 순서 보정: `WHERE IN` 절은 ID 목록의 순서를 보장하지 않으므로,
			//    처음에 정렬해서 얻은 ID 목록의 순서대로 `results`를 다시 정렬해줍니다.
			Map<Long, DiscussionQueryResult> resultMap = results.stream()
				.collect(Collectors.toMap(DiscussionQueryResult::getDiscussionId, Function.identity()));

			List<DiscussionQueryResult> sortedResults = ids.stream()
				.map(resultMap::get)
				.collect(Collectors.toList());

			// 5. 최종 Page 객체 생성: content, pageable, total 정보를 모두 사용하여 PageImpl을 만듭니다.
			return new PageImpl<>(sortedResults, pageable, totalCount);
		} else {
			List<Long> ids = discussionJpaRepository.findDiscussionIdsByProblemIdWithSubquery(problemId, sortBy, pageable);
			List<DiscussionQueryResult> results = discussionJpaRepository.findDiscussionsByIdsWithSubquery(ids, userId);

			long totalCount = 10; //discussionQueryRepository.countByProblemId(problemId);

			// 4. (매우 중요) 순서 보정: `WHERE IN` 절은 ID 목록의 순서를 보장하지 않으므로,
			//    처음에 정렬해서 얻은 ID 목록의 순서대로 `results`를 다시 정렬해줍니다.
			Map<Long, DiscussionQueryResult> resultMap = results.stream()
				.collect(Collectors.toMap(DiscussionQueryResult::getDiscussionId, Function.identity()));

			List<DiscussionQueryResult> sortedResults = ids.stream()
				.map(resultMap::get)
				.collect(Collectors.toList());

			// 5. 최종 Page 객체 생성: content, pageable, total 정보를 모두 사용하여 PageImpl을 만듭니다.
			return new PageImpl<>(sortedResults, pageable, totalCount);
		}
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
