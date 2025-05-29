package org.ezcode.codetest.domain.user.model.enums;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum Tier {
	NEWBIE, LEARNER, CODER, PRO, HACKER, GOAT;

	public static Tier from(String tier) {
		return Arrays.stream(Tier.values())
			.filter(r -> r.name().equalsIgnoreCase(tier))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Invalid tier input : " + tier));
	}
}

/*
등급	이름	설명
1	Newbie  이제 막 시작한 초보자. 기초 개념 학습 단계.
2	Learner	기본 개념과 문법은 익혔고, 간단한 문제는 해결 가능.
3	Coder 	실전 경험이 조금씩 생기며, 중급 문제를 도전함.
4	Pro	다양한 문제 해결 능력 보유, 효율성까지 고려하는 실력자.
5	Hacker  복잡한 문제도 해결하며 알고리즘 최적화에 능숙.
6   GOAT   신 good

*/