package org.ezcode.codetest.domain.game.util;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.ezcode.codetest.domain.game.model.character.Stat;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class StatUpdateUtil {

	private final Map<String, StatIncreasePerProblem> increasedStatRate;

	public StatUpdateUtil() {

		Map<String, StatIncreasePerProblem> modifiableMap = new HashMap<>();

		StatIncreasePerProblem implStat = new StatIncreasePerProblem();
		implStat.putIncreaseStat(Stat.PROBLEM_SOLVING, 1.0);
		implStat.putIncreaseStat(Stat.DEBUGGING, 0.7);
		modifiableMap.put("구현", implStat);

		StatIncreasePerProblem greedyStat = new StatIncreasePerProblem();
		greedyStat.putIncreaseStat(Stat.OPTIMIZATION, 1.2);
		greedyStat.putIncreaseStat(Stat.SPEED, 0.8);
		modifiableMap.put("그리디 알고리즘", greedyStat);

		StatIncreasePerProblem dfsStat = new StatIncreasePerProblem();
		dfsStat.putIncreaseStat(Stat.PROBLEM_SOLVING, 0.9);
		dfsStat.putIncreaseStat(Stat.DATA_STRUCTURE, 1.1);
		modifiableMap.put("깊이 우선 탐색", dfsStat);

		StatIncreasePerProblem bfsStat = new StatIncreasePerProblem();
		bfsStat.putIncreaseStat(Stat.DATA_STRUCTURE, 1.0);
		bfsStat.putIncreaseStat(Stat.SPEED, 0.7);
		modifiableMap.put("넓이 우선 탐색", bfsStat);

		StatIncreasePerProblem dpStat = new StatIncreasePerProblem();
		dpStat.putIncreaseStat(Stat.OPTIMIZATION, 1.3);
		dpStat.putIncreaseStat(Stat.PROBLEM_SOLVING, 1.0);
		modifiableMap.put("동적 계획법", dpStat);

		StatIncreasePerProblem graphStat = new StatIncreasePerProblem();
		graphStat.putIncreaseStat(Stat.DATA_STRUCTURE, 1.2);
		graphStat.putIncreaseStat(Stat.PROBLEM_SOLVING, 0.8);
		modifiableMap.put("그래프 이론", graphStat);

		StatIncreasePerProblem treeStat = new StatIncreasePerProblem();
		treeStat.putIncreaseStat(Stat.DATA_STRUCTURE, 1.1);
		treeStat.putIncreaseStat(Stat.PROBLEM_SOLVING, 0.7);
		modifiableMap.put("트리", treeStat);

		StatIncreasePerProblem dsStat = new StatIncreasePerProblem();
		dsStat.putIncreaseStat(Stat.DATA_STRUCTURE, 1.5);
		modifiableMap.put("자료구조(스택, 힙, 큐)", dsStat);

		StatIncreasePerProblem stringStat = new StatIncreasePerProblem();
		stringStat.putIncreaseStat(Stat.PROBLEM_SOLVING, 0.8);
		stringStat.putIncreaseStat(Stat.DEBUGGING, 0.6);
		modifiableMap.put("문자열", stringStat);

		StatIncreasePerProblem mathStat = new StatIncreasePerProblem();
		mathStat.putIncreaseStat(Stat.PROBLEM_SOLVING, 1.2);
		mathStat.putIncreaseStat(Stat.OPTIMIZATION, 0.7);
		modifiableMap.put("수학", mathStat);

		StatIncreasePerProblem simStat = new StatIncreasePerProblem();
		simStat.putIncreaseStat(Stat.PROBLEM_SOLVING, 0.9);
		simStat.putIncreaseStat(Stat.OPTIMIZATION, 0.9);
		modifiableMap.put("시뮬레이션", simStat);

		StatIncreasePerProblem bitwiseStat = new StatIncreasePerProblem();
		bitwiseStat.putIncreaseStat(Stat.PROBLEM_SOLVING, 1.0);
		bitwiseStat.putIncreaseStat(Stat.OPTIMIZATION, 0.8);
		modifiableMap.put("비트 연산", bitwiseStat);

		StatIncreasePerProblem recursionStat = new StatIncreasePerProblem();
		recursionStat.putIncreaseStat(Stat.PROBLEM_SOLVING, 1.1);
		modifiableMap.put("재귀", recursionStat);

		StatIncreasePerProblem backtrackStat = new StatIncreasePerProblem();
		backtrackStat.putIncreaseStat(Stat.PROBLEM_SOLVING, 1.0);
		backtrackStat.putIncreaseStat(Stat.DEBUGGING, 0.5);
		modifiableMap.put("백트래킹", backtrackStat);

		StatIncreasePerProblem interviewStat = new StatIncreasePerProblem();
		interviewStat.putIncreaseStat(Stat.PROBLEM_SOLVING, 0.7);
		interviewStat.putIncreaseStat(Stat.DEBUGGING, 0.7);
		modifiableMap.put("면접 대비", interviewStat);

		StatIncreasePerProblem mcqStat = new StatIncreasePerProblem();
		mcqStat.putIncreaseStat(Stat.SPEED, 0.6);
		mcqStat.putIncreaseStat(Stat.PROBLEM_SOLVING, 0.4);
		modifiableMap.put("객관식 문제", mcqStat);

		StatIncreasePerProblem fillBlankStat = new StatIncreasePerProblem();
		fillBlankStat.putIncreaseStat(Stat.DATA_STRUCTURE, 0.7);
		fillBlankStat.putIncreaseStat(Stat.SPEED, 0.5);
		modifiableMap.put("빈칸 문제", fillBlankStat);

		StatIncreasePerProblem beginnerStat = new StatIncreasePerProblem();
		beginnerStat.putIncreaseStat(Stat.PROBLEM_SOLVING, 0.8);
		beginnerStat.putIncreaseStat(Stat.DEBUGGING, 0.8);
		modifiableMap.put("입문자용", beginnerStat);

		this.increasedStatRate = Collections.unmodifiableMap(modifiableMap);
	}

	public Map<Stat, Double> getStatIncreasePerProblem(String categoryDescription) {
		//TODO : 나중에 NULL 처리하기
		return increasedStatRate.get(categoryDescription).asImmutableMap();
	}

	@Getter
	private static class StatIncreasePerProblem {
		private final Map<Stat, Double> increaseStat = new EnumMap<>(Stat.class);

		public void putIncreaseStat(Stat stat, Double amount) {
			increaseStat.put(stat, amount);
		}

		public Map<Stat, Double> asImmutableMap() {
			return Collections.unmodifiableMap(increaseStat);
		}
	}
}
