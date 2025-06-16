package org.ezcode.codetest.application.community.service;

import org.ezcode.codetest.application.community.dto.response.VoteResponse;
import org.ezcode.codetest.domain.community.model.entity.BaseVote;
import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.ezcode.codetest.domain.community.service.BaseVoteDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseVoteService<T extends BaseVote, D extends BaseVoteDomainService<T, ?>> {

	protected final D voteDomainService;
	private final UserDomainService userDomainService;

	/**
	 * 추천 애플리케이션 서비스 공통 로직
	 * - 각자 도메인에 맞는 검증 로직을 수행하고 호출됨
	 * - 추천 토글 (추천 또는 추천 취소)
	 * - 추천 했다면 알림 등의 후속 동작 수행
	 *
	 * @param voterId 추천한 유저 id
	 * @param targetId 추천 대상 entity id (자유글, 댓글 등)
	 * @return 추천이 됐는지, 취소가 됐는지 등의 정보를 담은 VoteResponse 반환
	 */
	protected VoteResponse toggleVote(Long voterId, Long targetId) {

		User voter = userDomainService.getUserById(voterId);

		boolean isVoted = voteDomainService.toggleVote(voter, targetId);

		if (isVoted) {
			afterVote(voter, targetId);
		}

		return VoteResponse.of(isVoted);
	}

	@Transactional(readOnly = true)
	public VoteResponse getVoteStatus(Long userId, Long targetId) {

		boolean voteStatus = voteDomainService.getVoteStatus(userId, targetId);
		return new VoteResponse(voteStatus);
	}

	protected abstract void afterVote(User voter, Long targetId);
}
