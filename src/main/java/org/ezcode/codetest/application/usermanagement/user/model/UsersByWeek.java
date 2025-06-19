package org.ezcode.codetest.application.usermanagement.user.model;

import java.util.List;

import org.ezcode.codetest.domain.submission.dto.WeeklySolveCount;

public record UsersByWeek(

	List<Long> fullWeek,

	List<Long> partialWeek

) {
	public static UsersByWeek from(List<WeeklySolveCount> counts, long weekLength) {
		List<Long> fullWeek = counts.stream()
			.filter(c -> c.solveDayCount() == weekLength)
			.map(WeeklySolveCount::userId)
			.toList();
		List<Long> partialWeek = counts.stream()
			.filter(c -> c.solveDayCount() != weekLength)
			.map(WeeklySolveCount::userId)
			.toList();
		return new UsersByWeek(fullWeek, partialWeek);
	}
}
