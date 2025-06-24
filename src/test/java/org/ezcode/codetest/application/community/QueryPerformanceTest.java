package org.ezcode.codetest.application.community;

import org.ezcode.codetest.util.TestDataGenerator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class QueryPerformanceTest {

	@Autowired
	private TestDataGenerator testDataGenerator;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	// @Disabled("데이터 생성 필요 시에만 활성화") // 매번 실행되지 않도록 @Disabled 처리
	void generateDummyData() {
		// 주의: 이 테스트는 실행에 수 분 ~ 수십 분이 소요될 수 있습니다.
		jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
		jdbcTemplate.execute("TRUNCATE TABLE discussion_vote");
		jdbcTemplate.execute("TRUNCATE TABLE reply");
		jdbcTemplate.execute("TRUNCATE TABLE discussion");
		jdbcTemplate.execute("TRUNCATE TABLE users");
		jdbcTemplate.execute("TRUNCATE TABLE problem");
		jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");

		testDataGenerator.generate();
	}
}
