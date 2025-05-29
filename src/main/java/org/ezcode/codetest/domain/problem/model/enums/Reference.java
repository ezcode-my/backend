package org.ezcode.codetest.domain.problem.model.enums;

import lombok.Getter;

@Getter
public enum Reference {
	ORIGINAL("자체 제작"),
	PROGRAMMERS("프로그래머스"),
	BAEKJOON("백준"),
	LEETCODE("리트코드"),
	CODEFORCES("코드포스"),
	CODEWARS("코드워즈"),
	DOVELET("더블릿"),
	AI("AI 제작");

	private final String description;

	Reference(String description) {
		this.description = description;
	}
}
