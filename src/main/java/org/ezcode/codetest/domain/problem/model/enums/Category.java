package org.ezcode.codetest.domain.problem.model.enums;

import lombok.Getter;

@Getter
public enum Category {
	IMPLEMENTATION("구현"),
	GREEDY("그리디 알고리즘"),
	DFS("깊이 우선 탐색"),
	BFS("넓이 우선 탐색"),
	DP("동적 계획법"),
	GRAPH("그래프 이론"),
	TREE("트리"),
	DATA_STRUCTURE("자료구조(스택, 힙, 큐)"),
	STRING("문자열"),
	MATH("수학"),
	SIMULATION("시뮬레이션"),
	BITWISE("비트 연산"),
	RECURSION("재귀"),
	BACKTRACKING("백트래킹"),
	INTERVIEW("면접 대비"),
	MULTIPLE_CHOICE("객관식 문제"),
	FILL_IN_THE_BLANK("빈칸 문제"),
	FOR_BEGINNER("입문자용");

	private final String description;

	Category(String description) {
		this.description = description;
	}
}
// 제 생각에는 이거 나쁘지 않은 거 같은데 제가 알고리즘 초보라서 그런가 봐요
//https://www.acmicpc.net/problem/tags 여기가서 보시면 됩니다.

