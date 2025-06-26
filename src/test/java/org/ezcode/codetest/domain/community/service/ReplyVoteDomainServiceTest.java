package org.ezcode.codetest.domain.community.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.ezcode.codetest.application.notification.enums.NotificationType;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.domain.community.dto.VoteResult;
import org.ezcode.codetest.domain.community.model.entity.Discussion;
import org.ezcode.codetest.domain.community.model.entity.Reply;
import org.ezcode.codetest.domain.community.model.entity.ReplyVote;
import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.ezcode.codetest.domain.community.repository.ReplyVoteRepository;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.support.fixture.CommunityFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("댓글 추천 도메인 서비스 테스트")
class ReplyVoteDomainServiceTest {

	@InjectMocks
	private ReplyVoteDomainService replyVoteDomainService;

	@Mock
	private ReplyVoteRepository mockVoteRepository;

	@Mock
	private ReplyDomainService mockReplyDomainService;

	@Mock
	private DiscussionDomainService mockDiscussionDomainService;

	private final String USER_EMAIL1 = "test1@email.com";
	private final String USER_EMAIL2 = "test2@email.com";

	@Nested
	@DisplayName("댓글 추천 메서드(manageVote) 테스트")
	class ManageVoteTest {

		/**
		 * 공통 로직이므로 대표 케이스만 테스트한다.
		 * 이 외의 로직 테스트는 아래 클래스 참고
		 * @see DiscussionVoteDomainServiceTest DiscussionVoteDomainServiceTest
		 */
		@DisplayName("추천 기록이 없을 떄, 추천을 하면 새로운 추천이 저장된다.")
		@Test
		void givenNoExistingVote_whenManageVoteWithUpvote_thenNewVoteShouldBeSaved() {
			// given
			User voter = CommunityFixture.createUser(USER_EMAIL1);
			Reply reply = Reply.builder().build();

			Long replyId = 1L;
			VoteType voteType = VoteType.UP;

			given(mockVoteRepository.findByVoterIdAndTargetId(voter.getId(), replyId)).willReturn(Optional.empty());
			given(mockReplyDomainService.getReplyById(replyId)).willReturn(reply);

			// when
			VoteResult voteResult = replyVoteDomainService.manageVote(voter, replyId, voteType);

			// then
			verify(mockVoteRepository, times(1)).save(any(ReplyVote.class));
			assertThat(voteResult.voteType()).isEqualTo(VoteType.UP);
			assertThat(voteResult.prevVoteType()).isEqualTo(VoteType.NONE);
		}

	}

	@DisplayName("유효한 ID들이 주어졌을 때, getValidatedReply를 호출하면 각 도메인 서비스에 책임을 위임하고 최종 댓글을 반환한다")
	@Test
	void givenValidIds_whenGetValidatedReply_thenDelegatesToServicesAndReturnsReply() {
		//given
		Long problemId = 1L;
		Long discussionId = 10L;
		Long replyId = 100L;
		
		Discussion mockDiscussion = mock(Discussion.class);
		Reply mockReply = mock(Reply.class);
		
		given(mockDiscussionDomainService.getDiscussionForProblem(discussionId, problemId)).willReturn(mockDiscussion);
		given(mockReplyDomainService.getReplyForDiscussion(replyId, mockDiscussion)).willReturn(mockReply);
		
		// when
		Reply result = replyVoteDomainService.getValidatedReply(problemId, discussionId, replyId);

		// then
		assertThat(mockReply).isEqualTo(result);

		// 책임 위임(메서드 호출)이 정확히 일어났는지 확인
		InOrder inOrder = inOrder(mockDiscussionDomainService, mockReplyDomainService);
		inOrder.verify(mockDiscussionDomainService).getDiscussionForProblem(eq(discussionId), eq(problemId));
		inOrder.verify(mockReplyDomainService).getReplyForDiscussion(eq(replyId), eq(mockDiscussion));
	}
	
	@Nested
	@DisplayName("알림 객체 생성 메서드 테스트")
	class CreateReplyVoteNotificationTest {

		@DisplayName("추천인과 댓글 작성자가 다를 때, 알림 객체 생성을 시도하면 NotificationCreateEvent가 들어있는 Optional이 반환된다.")
		@Test
		void givenVoterIsNotAuthor_whenCreateNotification_thenNotificationEventShouldBeCreated() {
			// given
			User voter = CommunityFixture.createUser(USER_EMAIL1);
			ReflectionTestUtils.setField(voter, "id", 1L);
			User author = CommunityFixture.createUser(USER_EMAIL2);

			Reply mockReply = mock(Reply.class);

			given(mockReply.getUser()).willReturn(author);
			given(mockReply.getProblemId()).willReturn(10L);
			given(mockReply.getId()).willReturn(100L);
			given(mockReply.getUserEmail()).willReturn(author.getEmail());

			// when
			Optional<NotificationCreateEvent> optionalEvent = replyVoteDomainService.createReplyVoteNotification(voter, mockReply);

			// then
			assertThat(optionalEvent).isPresent();

			NotificationCreateEvent event = optionalEvent.get();
			assertThat(event.principalName()).isEqualTo(author.getEmail());
			assertThat(event.notificationType()).isEqualTo(NotificationType.COMMUNITY_REPLY_VOTED_UP);
		}

		@DisplayName("추천인과 토론글 작성자가 동일할 때, 알림 발생을 시도하면 비어있는 Optional이 반환된다.")
		@Test
		void givenVoterIsAuthor_whenCreateNotification_thenEmptyOptionalShouldBeReturned() {
			// given
			User voter = CommunityFixture.createUser(USER_EMAIL1);
			ReflectionTestUtils.setField(voter, "id", 1L);

			Reply mockReply = mock(Reply.class);

			given(mockReply.getUser()).willReturn(voter);

			// when
			Optional<NotificationCreateEvent> optionalEvent = replyVoteDomainService.createReplyVoteNotification(voter, mockReply);

			// then
			assertThat(optionalEvent).isEmpty();
		}
	}
}
