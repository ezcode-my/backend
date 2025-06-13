package org.ezcode.codetest.domain.game.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Accessory {

	//임시 파일 곧 지워질 예정
	NOTHING("맨손", "일반", "맨손", "아무것도 장착하지 않은 상태입니다.", 0, 0, 0, 0, 0, 0, 0),
	CODE_RING("반지", "일반", "코드 반지", "코딩의 힘을 증폭시키는 반지.", 1, 1, 2, 1, 0, 1, 5),
	BUG_NECKLACE("목걸이", "일반", "버그 목걸이", "버그 탐지를 도와준다.", 0, 2, 3, 0, 0, 2, 4),
	ALGO_BRACELET("팔찌", "고급", "알고리즘 팔찌", "효율을 높이는 팔찌.", 1, 1, 3, 2, 0, 3, 5),
	DATA_CHARM("부적", "고급", "데이터 참", "자료 흐름을 안정시킨다.", 0, 2, 2, 1, 1, 2, 3),
	DEBUG_AMULET("부적", "고급", "디버그 부적", "오류 발생 시 경고해준다.", 1, 1, 2, 3, 0, 3, 4),
	STACK_BAND("밴드", "희귀", "스택 밴드", "스택 관리를 돕는다.", 2, 1, 2, 2, 1, 2, 4),
	QUEUE_EARRING("귀걸이", "일반", "큐 귀걸이", "명령을 순서대로 들려준다.", 1, 0, 3, 1, 0, 1, 3),
	HASH_BROOCH("브로치", "희귀", "해시 브로치", "데이터 무결성을 유지.", 2, 2, 1, 2, 0, 1, 4),
	TREE_PENDANT("펜던트", "고급", "트리 펜던트", "계층 구조를 직관화.", 1, 1, 2, 1, 0, 2, 3),
	GRAPH_CHARM("부적", "희귀", "그래프 참", "연결 강도를 높인다.", 2, 2, 1, 2, 1, 2, 5),
	HEAP_TALISMAN("부적", "고급", "힙 탈리스만", "메모리 최적화를 돕는다.", 1, 2, 1, 1, 1, 1, 3),
	PARALLEL_CHARM("부적", "전설", "병렬 참", "동시성 성능을 폭발시킨다.", 3, 1, 3, 3, 1, 3, 6),
	RECURSION_RING("반지", "고급", "재귀 반지", "반복력을 키워준다.", 1, 1, 4, 2, 0, 4, 5),
	BINARY_NECKLACE("목걸이", "일반", "이진 목걸이", "0과 1의 밸런스를 유지.", 1, 1, 2, 1, 0, 1, 4),
	LINKED_BRACELET("팔찌", "고급", "링크드 팔찌", "연결 속도를 높인다.", 1, 1, 3, 2, 0, 2, 5),
	SORT_CHARM("부적", "일반", "정렬 참", "작업 순서를 체계화.", 1, 0, 3, 1, 0, 1, 3),
	SEARCH_AMULET("부적", "고급", "탐색 부적", "목표를 빠르게 찾는다.", 1, 1, 2, 2, 0, 2, 4),
	CONCURRENT_RING("반지", "희귀", "동시성 반지", "멀티스레드를 지원.", 2, 1, 2, 3, 1, 3, 5),
	MEMORY_LOCKET("로켓", "고급", "메모리 로켓", "데이터 저장 능력 강화.", 1, 2, 1, 1, 1, 1, 4),
	STACK_OVERFLOW_NECKLACE("목걸이", "전설", "스택 오버플로우 목걸이", "끝없는 용량을 준다.", 3, 2, 2, 4, 2, 2, 6),
	THREAD_BRACELET("팔찌", "전설", "스레드 팔찌", "스레드 관리를 완벽히.", 2, 2, 3, 3, 1, 3, 5),
	API_CHARM("부적", "고급", "API 참", "원격 호출 효율 강화.", 1, 1, 3, 2, 0, 2, 4),
	GC_RING("반지", "희귀", "GC 반지", "불필요 자원을 정리.", 2, 1, 2, 2, 1, 1, 4),
	SYNTAX_NECKLACE("목걸이", "일반", "문법 목걸이", "문법 오류를 방지.", 1, 1, 2, 1, 0, 1, 3),
	BYTE_BRACELET("팔찌", "고급", "바이트 팔찌", "단위 작업 속도 증가.", 1, 1, 3, 2, 0, 1, 4),
	PROXY_PENDANT("펜던트", "희귀", "프록시 펜던트", "접근 제어 강화.", 2, 1, 1, 2, 0, 1, 3),
	BINARY_SEARCH_RING("반지", "고급", "이진 탐색 반지", "빠른 조회 지원.", 1, 1, 3, 3, 0, 2, 5),
	EXCEPTION_AMULET("부적", "희귀", "예외 부적", "치명적 예외 자동 복구.", 2, 1, 2, 3, 1, 2, 5),
	SOCKET_CHARM("부적", "전설", "소켓 참", "모든 연결을 강화.", 3, 2, 1, 3, 2, 2, 6),
	VERSION_PENDANT("펜던트", "일반", "버전 펜던트", "코드 버전을 안전하게 보관한다.", 0, 1, 1, 0, 0, 1, 2);

	private final String category;
	private final String grade;
	private final String name;
	private final String description;
	private final Integer atk;
	private final Integer def;
	private final Integer speed;
	private final Integer crit;
	private final Integer stun;
	private final Integer evasion;
	private final Integer accuracy;
}
