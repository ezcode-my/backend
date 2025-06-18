package org.ezcode.codetest.domain.game.model.encounter;

import java.util.Random;

import lombok.Getter;

@Getter
public enum MatchMessageTemplate {

	IN_THE_FOREST(
		"%s 님은 어두운 숲속을 걷던 중, 낡은 갑옷을 입고 있는 %s 님을 발견했습니다. 상대는 아직 당신을 눈치채지 못했습니다. 전투를 시작하시겠습니까?"
	),
	AT_RIVER_SHORE(
		"%s 님은 강가에서 휴식을 취하려다가 맞은편에서 무기를 든 %s 님과 눈이 마주쳤습니다. 상대는 이미 당신의 존재를 알고 있습니다. 전투를 시작하시겠습니까?"
	),
	ABANDONED_VILLAGE(
		"%s 님은 버려진 마을을 둘러보던 중, 길 위에 서 있는 %s 님과 마주쳤습니다. 상대는 긴장한 모습으로 당신을 바라보고 있습니다. 전투를 시작하시겠습니까?"
	),
	DARK_CAVE(
		"%s 님은 어두운 동굴 속에서 횃불을 든 %s 님을 발견했습니다. 상대는 당신의 움직임을 경계하고 있습니다. 전투를 시작하시겠습니까?"
	),
	MISTY_PLAINS(
		"%s 님은 안개가 자욱한 평원을 걷다가 흐릿한 형체를 발견했습니다. 가까이 다가가자 %s 님이 무기를 쥐고 서 있었습니다. 전투를 시작하시겠습니까?"
	),
	ANCIENT_RUINS(
		"%s 님은 폐허의 돌기둥 사이에서 %s 님을 발견했습니다. 상대는 이미 당신을 눈치채고 경계하고 있습니다. 전투를 시작하시겠습니까?"
	),
	SNOWY_MOUNTAINTOP(
		"%s 님은 눈 덮인 산 정상에서 강한 바람 속에 서 있는 %s 님을 발견했습니다. 상대는 무기를 든 채 당신을 주시하고 있습니다. 전투를 시작하시겠습니까?"
	),
	DESERT_OASIS(
		"%s 님은 사막의 오아시스에 도착했으나, 그곳에 먼저 온 %s 님이 있었습니다. 상대는 이미 당신을 알아챘습니다. 전투를 시작하시겠습니까?"
	),
	SUNKEN_SHIP(
		"%s 님은 난파선 근처에서 해안을 둘러보다가 %s 님과 마주쳤습니다. 상대는 무기를 손에 쥐고 있습니다. 전투를 시작하시겠습니까?"
	),
	VOLCANIC_FISSURE(
		"%s 님은 용암이 흐르는 균열 근처에서 뜨거운 열기를 견디며 서 있는 %s 님을 발견했습니다. 전투를 시작하시겠습니까?"
	),
	MOONLIT_CLEARING(
		"%s 님은 달빛 아래 고요한 공터에서 %s 님을 발견했습니다. 상대는 이미 당신을 바라보고 있습니다. 전투를 시작하시겠습니까?"
	),
	HAUNTED_GRAVEYARD(
		"%s 님은 오래된 묘지에서 움직이는 그림자를 발견했습니다. 가까이 다가가자 %s 님이 무기를 든 채 서 있었습니다. 전투를 시작하시겠습니까?"
	),
	WINDY_CLIFF(
		"%s 님은 바람이 거세게 부는 절벽 위에서 균형을 잡고 있는 %s 님을 발견했습니다. 전투를 시작하시겠습니까?"
	),
	UNDERGROUND_LAKE(
		"%s 님은 지하 호수 옆에서 조용히 서 있는 %s 님을 발견했습니다. 상대는 당신을 눈치챘습니다. 전투를 시작하시겠습니까?"
	),
	ANCIENT_LIBRARY(
		"%s 님은 고대 도서관의 책장 사이에서 %s 님을 발견했습니다. 상대는 경계하며 당신을 바라보고 있습니다. 전투를 시작하시겠습니까?"
	),
	LUSH_JUNGLE(
		"%s 님은 정글에서 움직이는 나뭇잎 뒤로 %s 님을 발견했습니다. 상대도 당신을 알아챈 듯합니다. 전투를 시작하시겠습니까?"
	),
	FORGOTTEN_TEMPLE(
		"%s 님은 잊혀진 사원 입구에서 %s 님과 마주쳤습니다. 상대는 당신을 경계하고 있습니다. 전투를 시작하시겠습니까?"
	),
	RICKETY_BRIDGE(
		"%s 님은 낡은 다리 위에서 균형을 잡고 서 있는 %s 님을 발견했습니다. 전투를 시작하시겠습니까?"
	),
	STORMY_SEASHORE(
		"%s 님은 폭풍우가 몰아치는 해변에서 %s 님과 마주쳤습니다. 상대는 이미 당신을 주시하고 있습니다. 전투를 시작하시겠습니까?"
	);

	private static final Random RNG = new Random();
	private final String template;

	MatchMessageTemplate(String template) {
		this.template = template;
	}

	public static String random(String playerName, String enemyName) {
		MatchMessageTemplate[] values = values();
		MatchMessageTemplate random = values[RNG.nextInt(values.length)];
		return random.format(playerName, enemyName);
	}

	private String format(Object... args) {
		return String.format(template, args);
	}
}
