package org.ezcode.codetest.application.community.service;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.ezcode.codetest.application.community.dto.request.VoteRequest;
import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.ezcode.codetest.domain.community.repository.DiscussionRepository;
import org.ezcode.codetest.domain.community.repository.DiscussionVoteRepository;
import org.ezcode.codetest.infrastructure.persistence.repository.user.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DiscussionVoteServiceConcurrencyTest {

	@Autowired
	private DiscussionVoteService discussionVoteService;

	@Autowired
	private DiscussionVoteRepository discussionVoteRepository;

	@Autowired
	private UserJpaRepository userRepository;

	@Autowired
	private DiscussionRepository discussionRepository;

	@Transactional
	@DisplayName("한 명의 유저가 동시에 추천을 요청할 경우 Race Condition이 발생하여 데이터 정합성이 깨진다")
	@Test
	void manageVote_concurrency_issue_test() throws InterruptedException {
		// given (준비)
		// 1. 테스트용 유저와 게시글 생성 및 저장
		// User voter = userRepository.save(CommunityFixture.createUser("test@email.com"));
		// Discussion targetDiscussion = discussionRepository.save(Discussion.builder().build());

		AtomicReference<Long> exceptionCnt = new AtomicReference<>(0L);

		// 2. 동시에 실행할 스레드 수 설정 (예: 100개)
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch startLatch = new CountDownLatch(1); // 모든 스레드가 준비될 때까지 대기
		CountDownLatch doneLatch = new CountDownLatch(threadCount); // 모든 스레드가 끝날 때까지 대기

		// when (실행)
		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					startLatch.await(); // 모든 스레드가 여기서 대기
					// 모든 스레드가 동일한 유저, 동일한 게시글에 UP 추천을 요청
					discussionVoteService.manageVoteOnDiscussion(1L, 1L, new VoteRequest(VoteType.UP), 1L);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} catch (DataIntegrityViolationException | ObjectOptimisticLockingFailureException e) {
					// DB 유니크 제약조건이 있거나, JPA의 낙관적 락이 있다면 예외가 발생할 수 있음
					// 테스트에서는 이 예외를 정상적인 실패 시나리오 중 하나로 간주
					exceptionCnt.getAndSet(exceptionCnt.get() + 1);
					System.out.println("예상된 동시성 예외 발생: " + e.getMessage());
				} finally {
					doneLatch.countDown();
				}
			});
		}

		// 모든 스레드를 동시에 시작
		startLatch.countDown();
		// 모든 스레드가 끝날 때까지 대기
		doneLatch.await();
		executorService.shutdown();

		Thread.sleep(500);

		// then (검증)
		// 최종적으로 vote 테이블에는 단 하나의 레코드만 있어야 한다.
		long upvoteCount = discussionVoteRepository.countUpvotesByTargetId(1L);

		System.out.println("예외 발생 수 : " + exceptionCnt);
		System.out.println("최종 투표 레코드 수: " + upvoteCount);

		// 결과적으로 데이터는 하나만 저장됨
		// voter_id, discussion_id에 유니크 제약조건을 걸었기 때문
		// 대신에 DataIntegrityViolationException 예외 발생
		assertThat(upvoteCount).isEqualTo(1L);
	}
}
