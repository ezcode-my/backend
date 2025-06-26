package org.ezcode.codetest.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class TestDataGeneratorForDiscussion {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private TransactionTemplate transactionTemplate;

	private static final Logger log = LoggerFactory.getLogger(TestDataGeneratorForDiscussion.class);

	private static final int USER_COUNT = 100_000;
	private static final int PROBLEM_COUNT = 1_000;
	private static final int LANGUAGE_COUNT = 5;
	private static final int DISCUSSION_COUNT = 40000;

	private static final int AVG_REPLIES_PER_DISCUSSION = 3;
	private static final int AVG_VOTES_PER_DISCUSSION = 8;

	private static final int CHUNK_SIZE = 50_000; // 트랜잭션을 커밋하는 단위

	public void generate() {
		log.info("Test data generation started.");
		long startTime = System.currentTimeMillis();

		jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

		createDiscussions();

		jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");

		long endTime = System.currentTimeMillis();
		log.info("Test data generation finished. Total time: {} ms", (endTime - startTime));
	}

	private void createDiscussions() {
		log.info("Generating {} discussions...", DISCUSSION_COUNT);
		String sql = "INSERT INTO discussion (user_id, problem_id, language_id, content, is_deleted, created_at, modified_at) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?)";

		for (int i = 0; i < DISCUSSION_COUNT; i += CHUNK_SIZE) {
			final int start = i;
			final int end = Math.min(start + CHUNK_SIZE, DISCUSSION_COUNT);
			final int currentChunkSize = end - start;

			transactionTemplate.execute(status -> {
				jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int j) throws SQLException {
						int discussionId = start + j + 1;
						ps.setLong(1, ThreadLocalRandom.current().nextLong(1, USER_COUNT + 1));
						ps.setLong(2, 500);
						ps.setLong(3, ThreadLocalRandom.current().nextLong(1, LANGUAGE_COUNT + 1));
						ps.setString(4, "This is a discussion content " + discussionId);
						ps.setBoolean(5, false);
						Timestamp now = Timestamp.from(Instant.now());
						ps.setTimestamp(6, now);
						ps.setTimestamp(7, now);
					}
					@Override
					public int getBatchSize() {
						return currentChunkSize;
					}
				});
				return null;
			});
			log.info("... Committed discussions from {} to {}", start, end);
		}
	}

	private void createReplies() {
		int totalReplies = DISCUSSION_COUNT * AVG_REPLIES_PER_DISCUSSION;
		log.info("Generating {} replies...", totalReplies);
		String sql = "INSERT INTO reply (discussion_id, user_id, content, is_deleted, created_at, modified_at) " +
			"VALUES (?, ?, ?, ?, ?, ?)";

		for (int i = 0; i < totalReplies; i += CHUNK_SIZE) {
			final int start = i;
			final int end = Math.min(start + CHUNK_SIZE, totalReplies);
			final int currentChunkSize = end - start;

			transactionTemplate.execute(status -> {
				jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int j) throws SQLException {
						ps.setLong(1, ThreadLocalRandom.current().nextLong(1, DISCUSSION_COUNT + 1));
						ps.setLong(2, ThreadLocalRandom.current().nextLong(1, USER_COUNT + 1));
						ps.setString(3, "This is a reply content " + (start + j + 1));
						ps.setBoolean(4, false);
						Timestamp now = Timestamp.from(Instant.now());
						ps.setTimestamp(5, now);
						ps.setTimestamp(6, now);
					}
					@Override
					public int getBatchSize() {
						return currentChunkSize;
					}
				});
				return null;
			});
			log.info("... Committed replies from {} to {}", start, end);
		}
	}

	private void createVotes() {
		int totalVotes = DISCUSSION_COUNT * AVG_VOTES_PER_DISCUSSION;
		log.info("Generating {} discussion votes systematically...", totalVotes);

		// INSERT IGNORE를 일반 INSERT로 변경 (중복이 없으므로)
		String sql = "INSERT INTO discussion_vote (voter_id, discussion_id, vote_type, created_at) " +
			"VALUES (?, ?, ?, ?)";

		final Random random = new Random();

		// CHUNK_SIZE는 그대로 트랜잭션 단위로 사용
		for (int i = 0; i < totalVotes; i += CHUNK_SIZE) {
			final int start = i;
			final int end = Math.min(start + CHUNK_SIZE, totalVotes);
			final int currentChunkSize = end - start;

			// 트랜잭션 시작
			transactionTemplate.execute(status -> {
				jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int j) throws SQLException {
						// i: 전체 투표 데이터에 대한 절대 인덱스 (0 ~ 7,999,999)
						// j: 현재 청크 내의 상대 인덱스 (0 ~ 49,999)
						int absoluteIndex = start + j;

						// 데이터를 순차적으로 생성하여 랜덤 I/O를 최소화
						// 1. discussion_id를 순차적으로 할당 (0, 0, ... 1, 1, ... )
						long discussionId = (long) (absoluteIndex / AVG_VOTES_PER_DISCUSSION) + 1;
						if (discussionId > DISCUSSION_COUNT) {
							discussionId = DISCUSSION_COUNT; // 마지막 토론에 나머지 투표 몰아주기
						}

						// 2. voter_id를 순차적으로 할당하여 중복을 피함
						// 한 토론 내에서 투표자(voter)가 겹치지 않도록
						long voterIdOffset = absoluteIndex % AVG_VOTES_PER_DISCUSSION;
						long voterId = (discussionId - 1) * AVG_VOTES_PER_DISCUSSION + voterIdOffset + 1;
						// voterId가 USER_COUNT를 넘지 않도록 보정 (선택적)
						voterId = (voterId % USER_COUNT) + 1;

						ps.setLong(1, voterId);
						ps.setLong(2, discussionId);
						ps.setString(3, random.nextBoolean() ? "UP" : "DOWN");
						ps.setTimestamp(4, Timestamp.from(Instant.now()));
					}

					@Override
					public int getBatchSize() {
						return currentChunkSize;
					}
				});
				return null;
			});
			log.info("... Committed votes from {} to {}", start, end);
		}
	}
}
