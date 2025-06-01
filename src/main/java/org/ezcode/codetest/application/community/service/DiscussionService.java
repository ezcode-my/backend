package org.ezcode.codetest.application.community.service;

import java.util.Objects;

import org.ezcode.codetest.application.community.dto.request.DiscussionCreateRequest;
import org.ezcode.codetest.application.community.dto.request.DiscussionUpdateRequest;
import org.ezcode.codetest.application.community.dto.response.DiscussionResponse;
import org.ezcode.codetest.domain.community.exception.CommunityException;
import org.ezcode.codetest.domain.community.exception.CommunityExceptionCode;
import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.community.service.DiscussionDomainService;
import org.ezcode.codetest.domain.language.service.LanguageDomainService;
import org.ezcode.codetest.domain.problem.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.service.LanguageDomainService;
import org.ezcode.codetest.domain.problem.service.ProblemDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscussionService {

	private final DiscussionDomainService discussionDomainService;
	private final UserDomainService userDomainService;
	private final ProblemDomainService problemDomainService;
	private final LanguageDomainService languageDomainService;

	@Transactional
	public DiscussionResponse createDiscussion(Long problemId, DiscussionCreateRequest request, Long userId) {

		User user = userDomainService.findUserById(userId);
		Problem problem = problemDomainService.retrieveById(problemId);
		Language language = languageDomainService.getLanguage(request.languageId());

		Discussion discussionEntity = DiscussionCreateRequest.toEntity(user, problem, language, request);

		return DiscussionResponse.fromEntity(discussionDomainService.store(discussionEntity));
	}

	@Transactional(readOnly = true)
	public Page<DiscussionResponse> getDiscussions(Long problemId, Pageable pageable) {

		Page<Discussion> discussionResponsePage = discussionDomainService.retrieveAllByProblemId(problemId, pageable);
		return discussionResponsePage.map(DiscussionResponse::fromEntity);
	}

	@Transactional
	public DiscussionResponse updateDiscussion(Long problemId, Long discussionId, DiscussionUpdateRequest request, Long userId) {
		Discussion discussion = discussionDomainService.retrieveById(discussionId);
		User user = userDomainService.findUserById(userId);
		
		// TODO: 검증 로직을 해당 도메인(problem, user?)으로 옮겨야 함
		if (!Objects.equals(problemId, discussion.getProblem().getId())) {
			throw new CommunityException(CommunityExceptionCode.DISCUSSION_PROBLEM_MISMATCH);
		}
		if (!Objects.equals(user.getId(), discussion.getUser().getId())) {
			throw new CommunityException(CommunityExceptionCode.USER_NOT_AUTHOR);
		}

		Language language = languageDomainService.getLanguage(request.languageId());
		discussion.update(language, request.content());
		Discussion updatedDiscussion = discussionDomainService.store(discussion);

		return DiscussionResponse.fromEntity(updatedDiscussion);
	}

	@Transactional
	public void deleteDiscussion(Long problemId, Long discussionId, Long userId) {
		Discussion discussion = discussionDomainService.retrieveById(discussionId);
		User user = userDomainService.findUserById(userId);

		// TODO: 검증 로직을 해당 도메인(problem, user?)으로 옮겨야 함
		if (!Objects.equals(problemId, discussion.getProblem().getId())) {
			throw new CommunityException(CommunityExceptionCode.DISCUSSION_PROBLEM_MISMATCH);
		}
		if (!Objects.equals(user.getId(), discussion.getUser().getId())) {
			throw new CommunityException(CommunityExceptionCode.USER_NOT_AUTHOR);
		}

		discussionDomainService.remove(discussion);
	}
}
