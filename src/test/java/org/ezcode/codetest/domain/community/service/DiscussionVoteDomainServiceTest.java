package org.ezcode.codetest.domain.community.service;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.ezcode.codetest.application.notification.enums.NotificationType;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.domain.community.dto.VoteResult;
import org.ezcode.codetest.domain.community.model.entity.Discussion;
import org.ezcode.codetest.domain.community.model.entity.DiscussionVote;
import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.ezcode.codetest.domain.community.repository.DiscussionVoteRepository;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.support.fixture.CommunityFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("토론글 추천 도메인 서비스 테스트")
class DiscussionVoteDomainServiceTest {

	@InjectMocks
	private DiscussionVoteDomainService discussionVoteDomainService;

	@Mock
	private DiscussionVoteRepository mockVoteRepository;

	@Mock
	private DiscussionDomainService mockDiscussionDomainService;

	private final String USER_EMAIL1 = "test1@email.com";
	private final String USER_EMAIL2 = "test2@email.com";

	@Nested
	@DisplayName("토론글 추천 메서드(manageVote) 테스트")
	class ManageVoteTest {

		@DisplayName("추천 기록이 없을 떄, 추천을 하면 새로운 추천이 저장된다.")
		@Test
		void givenNoExistingVote_whenManageVoteWithUpvote_thenNewVoteShouldBeSaved() {
			// given
			User voter = CommunityFixture.createUser(USER_EMAIL1);
			Discussion discussion = Discussion.builder().user(voter).build();

			Long discussionId = 1L;
			VoteType voteType = VoteType.UP;

			given(mockVoteRepository.findByVoterIdAndTargetId(voter.getId(), discussionId)).willReturn(Optional.empty());
			given(mockDiscussionDomainService.getDiscussionById(discussionId)).willReturn(discussion);

			// when
			VoteResult voteResult = discussionVoteDomainService.manageVote(voter, discussionId, voteType);

			// then
			verify(mockVoteRepository, times(1)).save(any(DiscussionVote.class));
			assertThat(voteResult.voteType()).isEqualTo(VoteType.UP);
			assertThat(voteResult.prevVoteType()).isEqualTo(VoteType.NONE);
		}

		@DisplayName("기존 추천 기록이 있을 때, 비추천을 하면 기존 추천이 수정된다.")
		@Test
		void givenExistingVote_whenManageVoteWithDownvote_thenVoteShouldBeUpdated() {
			// given
			User voter = CommunityFixture.createUser(USER_EMAIL1);
			Discussion discussion = Discussion.builder().user(voter).build();
			DiscussionVote discussionVote = DiscussionVote.builder()
				.discussion(discussion)
				.voter(voter)
				.voteType(VoteType.UP)
				.build();

			Long discussionId = 1L;
			VoteType newType = VoteType.DOWN;

			given(mockVoteRepository.findByVoterIdAndTargetId(voter.getId(), discussionId)).willReturn(Optional.of(discussionVote));

			// when
			VoteResult voteResult = discussionVoteDomainService.manageVote(voter, discussionId, newType);

			// then
			verify(mockVoteRepository, times(1)).update(eq(discussionVote), eq(newType));
			assertThat(voteResult.voteType()).isEqualTo(VoteType.DOWN);
			assertThat(voteResult.prevVoteType()).isEqualTo(VoteType.UP);
		}

		@DisplayName("기존 추천 기록이 있을 때, 추천 취소를 하면 기존 추천이 삭제된다.")
		@Test
		void givenExistingVote_whenManageVoteToCancel_thenVoteShouldBeDeleted() {
			// given
			User voter = CommunityFixture.createUser(USER_EMAIL1);
			Discussion discussion = Discussion.builder().user(voter).build();
			DiscussionVote discussionVote = DiscussionVote.builder()
				.discussion(discussion)
				.voter(voter)
				.voteType(VoteType.UP)
				.build();

			Long discussionId = 1L;
			VoteType newType = VoteType.NONE;

			given(mockVoteRepository.findByVoterIdAndTargetId(voter.getId(), discussionId)).willReturn(Optional.of(discussionVote));

			// when
			VoteResult voteResult = discussionVoteDomainService.manageVote(voter, discussionId, newType);

			// then
			verify(mockVoteRepository, times(1)).delete(eq(discussionVote));
			assertThat(voteResult.voteType()).isEqualTo(VoteType.NONE);
			assertThat(voteResult.prevVoteType()).isEqualTo(VoteType.NONE);
		}
	}

	@Nested
	@DisplayName("알림 객체 생성 메서드 테스트")
	class CreateDiscussionVoteNotificationTest {

		@DisplayName("추천인과 토론글 작성자가 다를 때, 알림 객체 생성을 시도하면 NotificationCreateEvent가 들어있는 Optional이 반환된다.")
		@Test
		void givenVoterIsNotAuthor_whenCreateNotification_thenNotificationEventShouldBeCreated() {
			// given
			User voter = CommunityFixture.createUser(USER_EMAIL1);
			ReflectionTestUtils.setField(voter, "id", 1L);
			User author = CommunityFixture.createUser(USER_EMAIL2);

			Discussion mockDiscussion = mock(Discussion.class);

			given(mockDiscussion.getUser()).willReturn(author);
			given(mockDiscussion.getProblemId()).willReturn(111L);
			given(mockDiscussion.getUserEmail()).willReturn(author.getEmail());

			// when
			Optional<NotificationCreateEvent> optionalEvent = discussionVoteDomainService.createDiscussionVoteNotification(voter, mockDiscussion);

			// then
			assertThat(optionalEvent).isPresent();

			NotificationCreateEvent event = optionalEvent.get();
			assertThat(event.principalName()).isEqualTo(author.getEmail());
			assertThat(event.notificationType()).isEqualTo(NotificationType.COMMUNITY_DISCUSSION_VOTED_UP);
		}

		@DisplayName("추천인과 토론글 작성자가 동일할 때, 알림 발생을 시도하면 비어있는 Optional이 반환된다.")
		@Test
		void givenVoterIsAuthor_whenCreateNotification_thenEmptyOptionalShouldBeReturned() {
			// given
			User voter = CommunityFixture.createUser(USER_EMAIL1);
			ReflectionTestUtils.setField(voter, "id", 1L);

			Discussion mockDiscussion = mock(Discussion.class);

			given(mockDiscussion.getUser()).willReturn(voter);

			// when
			Optional<NotificationCreateEvent> event = discussionVoteDomainService.createDiscussionVoteNotification(voter, mockDiscussion);

			// then
			assertThat(event).isEmpty();
		}
	}
}
