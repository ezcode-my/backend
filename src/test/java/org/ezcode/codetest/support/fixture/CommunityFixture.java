package org.ezcode.codetest.support.fixture;

import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.enums.Tier;

public class CommunityFixture {
	
	public static User createUser(String email) {
		return User.builder()
			.email(email)
			.username("성이름")
			.password("P@ssw0rd1225")
			.nickname("닉네임")
			.tier(Tier.NEWBIE)
			.age(99)
			.build();
	}
}
