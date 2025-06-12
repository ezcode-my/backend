package org.ezcode.codetest.domain.game.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Skill {

	//임시
	DDOS_ATTACK("디도스 공격","전설", "공격 시, 공격 기회를 30% 확률로 한번 더 획득한다."),
	INFINITE_LOOP("무한 루프 폭발", "전설", "사용 즉시 상대를 무한 루프에 빠뜨려 2턴 동안 행동 불가 상태로 만든다."),
	DIVIDE_BY_ZERO("제로 나누기", "전설", "사용 시 연산 오류를 일으켜 적에게 막대한 피해를 주고, 자신의 다음 스킬 쿨다운 1턴이 증가한다."),
	STACK_OVERFLOW("스택 오버플로우", "전설", "적의 방어력을 완전히 무시하고 강력한 대미지를 입힌다."),

	COMPILER_OPTIMIZE("컴파일 최적화", "고급", "다음 공격의 대미지를 1.5배로 증가시킨다."),
	DEBUGGER("디버거", "고급", "적의 버프 하나를 해제하고 디버깅 피해를 준다."),
	GARBAGE_COLLECTION("가비지 컬렉션", "고급", "자신의 모든 디버프를 제거하고, 매 턴 소량 HP 회복 효과를 얻는다."),
	PATCH_UPDATE("패치 업데이트", "고급", "모든 스킬 쿨다운을 즉시 초기화한다."),
	HOTFIX("핫픽스", "고급", "즉시 치유 효과를 제공하고, 다음 턴 공격력 10% 증가 버프를 얻는다."),
	LIFE_STEAL_BUG("시스템 내부 흡혈버그", "고급", "공격 시 공격력의 10%만큼 HP를 회복한다."),
	MEMORY_LEAK("메모리 누수", "고급", "적에게 매 턴 지속 피해를 입히고, 일정 확률로 적의 행동 포인트를 감소시킨다."),

	THREAD_SYNCHRONIZATION("스레드 동기화", "희귀", "턴 동안 행동 포인트를 2배로 획득하여 추가 행동 기회를 얻는다."),
	DEADLOCK("데드락", "희귀", "적의 행동 속도를 0으로 만들어 1턴 동안 행동할 수 없게 만든다."),
	CODE_REFACTOR("코드 리팩토링", "희귀", "자신의 모든 스탯을 임시로 크게 향상시키고, 턴마다 소량씩 HP를 회복한다."),
	ASYNC_AWAIT("비동기 대기", "희귀", "즉시 AP(행동 포인트)를 1회 복구하고, 다음 공격이 명중 시 추가 효과를 발동한다."),
	DECRYPTION("복호화", "희귀", "적의 보호막을 해제하고 약화 상태로 만들어 받은 피해를 20% 증가시킨다."),
	BACKUP("백업 복원", "희귀", "사망 시 한 번 부활하며, 최대 HP의 50%로 재생성된다."),

	FAST_PARSING("고속 파싱", "일반", "턴 시작 시 스킬 재사용 대기시간을 1턴 단축한다."),
	POLYMORPHISM("다형성 변환", "일반", "아군 혹은 자신에게 랜덤 버프 하나를 부여한다."),
	REFLECTION("리플렉션", "일반", "적의 스킬 효과 하나를 복사하여 즉시 사용한다."),
	ENCRYPTION("암호화", "일반", "자신에게 보호막을 부여하여 일정량의 피해를 무시한다."),
	HOT_DEPLOY("핫 디플로이", "일반", "현재 상태에서 즉시 빠르게 스킬을 하나 실행한다."),
	LOGGING("로깅", "일반", "적의 최근 행동을 분석하여, 다음 공격의 치명타 확률을 소폭 증가시킨다.");

	private final String name;
	private final String grade;
	private final String description;
}
