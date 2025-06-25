package org.ezcode.codetest.domain.community.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.ezcode.codetest.domain.community.dto.DiscussionQueryResult;
import org.ezcode.codetest.domain.community.exception.CommunityException;
import org.ezcode.codetest.domain.community.exception.CommunityExceptionCode;
import org.ezcode.codetest.domain.community.model.entity.Discussion;
import org.ezcode.codetest.domain.community.repository.DiscussionRepository;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscussionDomainService {

	private final DiscussionRepository discussionRepository;

	public Discussion createDiscussion(Discussion discussion) {

		return discussionRepository.save(discussion);
	}

	public Discussion getDiscussionById(Long discussionId) {

		return discussionRepository.findById(discussionId)
			.orElseThrow(() -> new CommunityException(CommunityExceptionCode.DISCUSSION_NOT_FOUND));
	}

	public Discussion getDiscussionForProblem(Long discussionId, Long problemId) {

		Discussion discussion = getDiscussionById(discussionId);
		validateProblemMatches(discussion, problemId);

		return discussion;
	}

	public Page<DiscussionQueryResult> getAllDiscussionsByProblemId(Long problemId, String sortBy, Long userId, Pageable pageable) {

		// 성능 향상 위해 쿼리 분리
		List<Long> discussionIds = discussionRepository.findDiscussionIdsByProblemId(problemId, sortBy, pageable);
		List<DiscussionQueryResult> results = discussionRepository.findDiscussionsByIds(discussionIds, userId);

		Long totalCount = discussionRepository.countByProblemId(problemId);

		// `WHERE IN` 절은 ID 목록의 순서를 보장하지 않으므로, 처음에 정렬해서 얻은 ID 목록의 순서대로 results를 다시 정렬
		Map<Long, DiscussionQueryResult> resultMap = results.stream()
			.collect(Collectors.toMap(DiscussionQueryResult::getDiscussionId, Function.identity()));

		List<DiscussionQueryResult> sortedResults = discussionIds.stream()
			.map(resultMap::get)
			.collect(Collectors.toList());

		return new PageImpl<>(sortedResults, pageable, totalCount);
	}

	public Discussion modify(Long discussionId, Long problemId, Long userId, Language language, String content) {

		Discussion discussion = getDiscussionById(discussionId);

		validateProblemMatches(discussion, problemId);
		validateIsAuthor(discussion, userId);

		discussionRepository.updateDiscussion(discussion, language, content);

		return discussion;
	}

	public void remove(Long discussionId, Long problemId, Long userId) {

		Discussion discussion = getDiscussionById(discussionId);

		validateProblemMatches(discussion, problemId);
		validateIsAuthor(discussion, userId);

		discussionRepository.deleteDiscussion(discussion);
	}


	public void validateProblemMatches(Discussion discussion, Long problemId) {

		if (!discussion.isProblemMatches(problemId)) {
			throw new CommunityException(CommunityExceptionCode.DISCUSSION_PROBLEM_MISMATCH);
		}
	}

	public void validateIsAuthor(Discussion discussion, Long userId) {

		if (!discussion.isAuthor(userId)) {
			throw new CommunityException(CommunityExceptionCode.USER_NOT_AUTHOR);
		}
	}
}
