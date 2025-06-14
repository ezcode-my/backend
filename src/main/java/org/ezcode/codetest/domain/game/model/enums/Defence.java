package org.ezcode.codetest.domain.game.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Defence {

	//임시 파일 곧 지워질 예정
	NOTHING("맨손", "일반", "맨손", "아무것도 장착하지 않은 상태입니다.", 0, 0, 0, 0, 0, 0, 0),
	CODE_GUARD("갑옷", "일반", "코드 가드", "코드를 보호하는 기본 방패.", 1, 8, 1, 0, 0, 1, 3),
	BUG_SHIELD("방패", "일반", "버그 실드", "버그 공격을 흡수하는 실드.", 2, 9, 1, 0, 0, 2, 3),
	ALGO_SHIELD("방패", "고급", "알고리즘 쉴드", "효율적인 방어 알고리즘.", 1, 12, 0, 1, 0, 2, 4),
	DATA_ARMOR("갑옷", "고급", "데이터 아머", "자료 구조처럼 단단한 갑옷.", 0, 14, -1, 0, 1, 1, 2),
	DEBUG_PLATE("판금", "고급", "디버그 플레이트", "오류를 막아내는 판금.", 2, 13, 0, 0, 0, 1, 3),
	STACK_BARRIER("배리어", "희귀", "스택 배리어", "쌓인 충격을 분산.", 1, 15, -1, 1, 1, 2, 4),
	QUEUE_WARD("갑옷", "일반", "큐 와드", "순차 방어 태세.", 1, 10, 1, 0, 0, 1, 3),
	HASH_MAIL("갑옷", "희귀", "해시 메일", "충격 해시로 반사.", 3, 14, -1, 1, 0, 1, 4),
	TREE_PLATE("판금", "고급", "트리 플레이트", "분기되는 공격을 분산.", 0, 13, 0, 1, 0, 1, 3),
	GRAPH_BULWARK("방패", "희귀", "그래프 불워크", "네트워크를 단단히 지킨다.", 2, 16, -2, 0, 1, 2, 4),
	HEAP_ARMOR("갑옷", "고급", "힙 아머", "메모리처럼 단단한 방어.", 0, 14, -1, 0, 1, 1, 3),
	PARALLEL_PLATE("판금", "희귀", "병렬 플레이트", "동시 방어 시스템.", 1, 17, -2, 1, 1, 2, 5),
	RECURSION_SCALE("갑옷", "고급", "재귀 스케일", "계속해서 회복되는 방어.", 1, 13, 0, 1, 1, 2, 4),
	BINARY_SHIELD("방패", "일반", "이진 실드", "0과 1로 된 견고함.", 1, 11, 0, 0, 0, 1, 3),
	LINKEDMAIL("갑옷", "고급", "링크드 메일", "연결된 방어망.", 2, 12, 0, 0, 1, 2, 4),
	SORT_ARMOR("갑옷", "일반", "정렬 아머", "정돈된 방어 시스템.", 1, 10, 1, 0, 0, 1, 3),
	SEARCH_SHIELD("방패", "고급", "탐색 실드", "위협을 빠르게 감지.", 1, 13, 0, 1, 0, 2, 4),
	CONCURRENT_COAT("코트", "희귀", "동시성 코트", "다중 위협 동시 방어.", 2, 16, -2, 1, 1, 2, 5),
	MEMORY_VEST("조끼", "고급", "메모리 베스트", "지속 방어력을 제공.", 1, 14, 0, 0, 1, 1, 3),
	STACK_OVERFLOW_SHIELD("방패", "전설", "스택 오버플로우 실드", "넘치는 방어력.", 3, 18, -3, 1, 2, 2, 6),
	THREAD_GUARD("방패", "전설", "스레드 가드", "스레드 공격을 완벽 차단.", 2, 17, -2, 1, 2, 2, 5),
	API_BARRIER("배리어", "고급", "API 배리어", "원격 공격 차단.", 1, 13, 0, 1, 0, 1, 4),
	GC_MAIL("갑옷", "희귀", "GC 메일", "불필요 자원 차단.", 2, 15, -1, 1, 1, 1, 4),
	SYNTAX_SHIELD("방패", "일반", "문법 실드", "잘못된 입력을 방어.", 1, 11, 1, 0, 0, 1, 3),
	BYTE_SCALE("갑옷", "고급", "바이트 스케일", "작은 충격도 흡수.", 1, 12, 0, 0, 1, 1, 3),
	PROXY_PLATE("판금", "희귀", "프록시 플레이트", "대리 방어망.", 2, 14, -1, 1, 0, 1, 4),
	BINARY_TREE_SHIELD("방패", "고급", "이진 트리 실드", "균형 잡힌 방어.", 1, 13, 0, 1, 0, 1, 3),
	EXCEPTION_GUARD("방패", "희귀", "예외 가드", "치명적 예외 차단.", 2, 15, 0, 1, 1, 2, 5),
	SOCKET_SHIELD("방패", "전설", "소켓 실드", "모든 접속을 봉쇄.", 3, 18, -2, 2, 2, 2, 6),
	VERSION_ARMOR("갑옷", "일반", "버전 아머", "버전 관리를 단단히 지켜준다.", 0, 10, 0, 0, 0, 1, 1);

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
