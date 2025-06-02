package org.ezcode.codetest.application.community.service;

import java.util.Objects;

import org.ezcode.codetest.application.community.dto.request.DiscussionCreateRequest;
import org.ezcode.codetest.application.community.dto.request.DiscussionUpdateRequest;
import org.ezcode.codetest.application.community.dto.response.DiscussionResponse;
import org.ezcode.codetest.domain.community.exception.CommunityException;
import org.ezcode.codetest.domain.community.exception.CommunityExceptionCode;
import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.community.service.DiscussionDomainService;
import org.ezcode.codetest.domain.problem.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.service.LanguageDomainService;
import org.ezcode.codetest.domain.problem.service.ProblemDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscussionService {

	private final DiscussionDomainService discussionDomainService;
	private final UserDomainService userDomainService;
	private final ProblemDomainService problemDomainService;
	private final LanguageDomainService languageDomainService;

	public DiscussionResponse createDiscussion(Long problemId, DiscussionCreateRequest request) {

		User user = userDomainService.findById(1L);
		Problem problem = problemDomainService.findById(problemId);
		Language language = languageDomainService.findById(request.languageId);

		Discussion discussionEntity = DiscussionCreateRequest.toEntity(user, problem, language, request);

		return DiscussionResponse.fromEntity(discussionDomainService.store(discussionEntity));
	}

	public Page<DiscussionResponse> getDiscussions(Long problemId, Pageable pageable) {

		Page<Discussion> discussionResponsePage = discussionDomainService.retrieveAllByProblemId(problemId, pageable);
		return discussionResponsePage.map(DiscussionResponse::fromEntity);
	}

	public DiscussionResponse updateDiscussion(Long problemId, Long discussionId, DiscussionUpdateRequest request) {
		Discussion discussion = discussionDomainService.findById(discussionId);
		
		// TODO: 검증 로직을 해당 도메인(problem, user?)으로 옮겨야 함
		if (Objects.equals(problemId, discussion.getProblem().getId())) {
			throw new CommunityException(CommunityExceptionCode.DISCUSSION_PROBLEM_MISMATCH);
		}
		if (Objects.equals(userId, discussion.getUser().getId())) {
			throw new CommunityException(CommunityExceptionCode.USER_NOT_AUTHOR);
		}

		Language language = languageDomainService.findById(request.languageId);
		discussion.update(language, request.content);
		Discussion updatedDiscussion = discussionDomainService.save(discussion);

		return DiscussionResponse.fromEntity(updatedDiscussion);
	}

	public void deleteDiscussion(Long discussionId) {
		Discussion discussion = discussionDomainService.findById(discussionId);

		// TODO: 검증 로직을 해당 도메인(problem, user?)으로 옮겨야 함
		if (Objects.equals(problemId, discussion.getProblem().getId())) {
			throw new CommunityException(CommunityExceptionCode.DISCUSSION_PROBLEM_MISMATCH);
		}
		if (Objects.equals(userId, discussion.getUser().getId())) {
			throw new CommunityException(CommunityExceptionCode.USER_NOT_AUTHOR);
		}

		discussionDomainService.delete(discussion);
	}
}
