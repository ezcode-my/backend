package org.ezcode.codetest.domain.game.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Weapon implements Item {

	//임시
	BIT_SHOTGUN("총", "레어", "비트 샷건", "이 총에 맞는 자는 비트 단위로 분해된다는 무서운 샷건..!", 10, 1, 1, 5, 3, 1, 6),
	CODE_SWORD("장검", "일반", "코드 소드", "코드를 베어내는 일반적인 검. 주니어 개발자한테 보급되는 평범한 검이다.", 8, 1, 2, 3, 0, 2, 4),
	BUG_CLEAVER("둔기", "일반", "버그 클리버", "코드를 박살내는 일반적인 둔기. 주니어 개발자한테 보급되는 평범한 둔기다", 7, 2, 3, 2, 0, 3, 3),
	ALGO_BLADE("장검", "고급", "알고리즘 블레이드", "효율적인 알고리즘처럼 날카로운 검. 주니어 개발자는 보기만 해도 머리가 아파온다.", 12, 1, 2, 5, 0, 2, 6),
	DATA_HAMMER("둔기", "고급", "데이터 해머", "자료 구조를 두드려 깨우는 강력 타격.", 10, 3, 1, 2, 0, 1, 3),
	DEBUG_RIFLE("총", "고급", "디버그 라이플", "버그를 조준해 한 방에 해결한다.", 11, 2, 3, 4, 0, 3, 5),
	STACK_CRUSHER("둔기", "희귀", "스택 크러셔", "쌓인 오류를 한 번에 무너뜨린다.", 14, 1, 1, 3, 1, 1, 4),
	QUEUE_LAUNCHER("총", "일반", "큐 런처", "명령을 순차적으로 발사하는 무기.", 9, 1, 4, 2, 0, 4, 3),
	HASH_CANNON("총", "희귀", "해시 캐논", "빠른 해시 처리로 적을 마비시킨다.", 15, 0, 2, 5, 1, 2, 6),
	TREE_SPEAR("창", "고급", "트리 스피어", "분기 없이 곧장 찌르는 창.", 13, 1, 2, 4, 0, 2, 5),
	GRAPH_AXE("도끼", "고급", "그래프 도끼", "연결된 노드를 강력히 도끼질.", 12, 2, 1, 3, 0, 1, 4),
	HEAP_SPIKE("창", "희귀", "힙 스파이크", "최댓값을 찌르는 뾰족한 창.", 14, 1, 1, 4, 1, 1, 5),
	PARALLEL_LANCER("창", "희귀", "병렬 랜서", "동시에 다수의 공격을 가한다.", 16, 0, 3, 5, 1, 3, 6),
	RECURSION_DAGGER("단검", "고급", "재귀 단검", "잘못된 구조로 만들어진 재귀 호출...", 11, 1, 5, 2, 0, 5, 3),
	BINARY_BLADE("장검", "일반", "이진 블레이드", "0과 1로 구분된 예리함.", 8, 1, 3, 2, 0, 3, 3),
	LINKEDLIST_LANCE("창", "고급", "연결리스트 랜스", "끊임없이 재조합되는 창.", 10, 2, 2, 3, 0, 2, 4),
	SORT_SABER("창", "일반", "정렬 세이버", "순서를 정렬하며 베어낸다.", 9, 1, 2, 3, 0, 2, 4),
	SEARCH_SHURIKEN("표창", "희귀", "탐색 수리켄", "피할 수 없는 유도 슈리켄, 어떠한 악조건에서 적을 빠르게 찾아내서 타격한다.", 13, 0, 4, 4, 1, 4, 5),
	CONCURRENT_CROSSBOW("무기", "희귀", "동시성 크로스보우", "여러 발을 동시에 발사.", 15, 1, 3, 5, 0, 3, 6),
	MEMORY_GRENADE("무기", "고급", "메모리 수류탄", "폭발과 함께 메모리를 뒤흔든다.", 12, 2, 1, 2, 1, 1, 3),
	STACK_OVERFLOW_SWORD("무기", "전설", "스택 오버플로우 소드", "넘치는 힘으로 모든 것을 베어낸다.", 18, 1, 1, 6, 2, 1, 7),
	THREAD_STORMHAMMER("무기", "전설", "스레드 스톰해머", "멀티스레드 공격의 결정체.", 17, 2, 2, 5, 1, 2, 6),
	API_JAVELIN("무기", "고급", "API 자벨린", "원격 호출로 정확히 꽂는다.", 11, 1, 3, 4, 0, 3, 5),
	GC_CLEAVER("무기", "희귀", "GC 클리버", "불필요한 자원을 깔끔히 제거.", 14, 2, 1, 3, 1, 1, 4),
	SYNTAX_SWORD("무기", "레어", "문법 소드", "틀린 문법을 참수한다.", 14, 2, 3, 2, 0, 3, 3),
	BYTE_BLADE("무기", "고급", "바이트 블레이드", "작은 단위로 연속 타격.", 10, 1, 4, 3, 0, 4, 4),
	PROXY_PIKE("무기", "희귀", "프록시 파이크", "중계 공격으로 적을 속인다.", 13, 1, 2, 4, 1, 2, 5),
	BINARY_SEARCH_BOW("무기", "고급", "이진 탐색 활", "빠른 탐색으로 정확히 명중.", 12, 1, 3, 5, 0, 3, 6),
	EXCEPTION_DAGGER("무기", "희귀", "예외 단검", "치명적인 예외를 발생시킨다.", 14, 0, 5, 4, 1, 5, 5),
	THREAD_RIFLE("무기", "고급", "스레드 라이플", "연속 사격으로 적을 압도.", 13, 1, 4, 5, 0, 4, 6),
	SOCKET_CANNON("무기", "전설", "디도스 캐논", "서버의 모든 연결을 일시에 터뜨린다. 단 한방으로 데이터 센터 직원들을 초죽음으로 만들수 있다고 전해진다.", 25, 0, 2, 6, 2, 2, 7);

	private final String  category;
	private final String  grade;
	private final String  name;
	private final String  description;
	private final Integer atk;
	private final Integer def;
	private final Integer speed;
	private final Integer crit;
	private final Integer stun;
	private final Integer evasion;
	private final Integer accuracy;
}
