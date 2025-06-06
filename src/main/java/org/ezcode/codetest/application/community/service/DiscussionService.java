package org.ezcode.codetest.application.community.service;

import org.ezcode.codetest.application.community.dto.request.DiscussionCreateRequest;
import org.ezcode.codetest.application.community.dto.request.DiscussionModifyRequest;
import org.ezcode.codetest.application.community.dto.response.DiscussionResponse;
import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.community.service.DiscussionDomainService;
import org.ezcode.codetest.domain.submission.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.submission.service.LanguageDomainService;
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

		User user = userDomainService.getUserById(userId);
		Problem problem = problemDomainService.getProblem(problemId);
		Language language = languageDomainService.getLanguage(request.languageId());

		Discussion discussionEntity = DiscussionCreateRequest.toEntity(user, problem, language, request);

		return DiscussionResponse.fromEntity(discussionDomainService.createDiscussion(discussionEntity));
	}

	@Transactional(readOnly = true)
	public Page<DiscussionResponse> getDiscussions(Long problemId, Pageable pageable) {

		Page<Discussion> discussionResponsePage = discussionDomainService.getAllDiscussionsByProblemId(problemId, pageable);
		return discussionResponsePage.map(DiscussionResponse::fromEntity);
	}

	@Transactional
	public DiscussionResponse modifyDiscussion(Long problemId, Long discussionId, DiscussionModifyRequest request, Long userId) {

		Discussion discussion = discussionDomainService.getDiscussionById(discussionId);

		discussionDomainService.validateProblemMatches(discussion, problemId);
		discussionDomainService.validateIsAuthor(discussion, userId);

		Language language = languageDomainService.getLanguage(request.languageId());
		discussionDomainService.modify(discussion, language, request.content());

		return DiscussionResponse.fromEntity(discussion);
	}

	@Transactional
	public void removeDiscussion(Long problemId, Long discussionId, Long userId) {

		Discussion discussion = discussionDomainService.getDiscussionById(discussionId);

		discussionDomainService.validateProblemMatches(discussion, problemId);
		discussionDomainService.validateIsAuthor(discussion, userId);

		discussionDomainService.remove(discussion);
	}
}
