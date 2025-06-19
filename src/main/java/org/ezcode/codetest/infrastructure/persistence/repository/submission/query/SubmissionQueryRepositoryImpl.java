package org.ezcode.codetest.infrastructure.persistence.repository.submission.query;

import static com.querydsl.core.types.Projections.*;

import java.time.LocalDateTime;
import java.util.List;

import org.ezcode.codetest.domain.submission.dto.WeeklySolveCount;
import org.ezcode.codetest.domain.submission.model.entity.QSubmission;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SubmissionQueryRepositoryImpl implements SubmissionQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<WeeklySolveCount> fetchWeeklySolveCounts(
		LocalDateTime startDateTime,
		LocalDateTime endDateTime
	) {

		QSubmission s = QSubmission.submission;

		var dateOnly = Expressions.dateTemplate(java.sql.Date.class, "function('date',{0})", s.createdAt);
		var cntExpr = dateOnly.countDistinct();

		return jpaQueryFactory
			.select(constructor(
				WeeklySolveCount.class,
				s.user.id,
				cntExpr
			))
			.from(s)
			.where(
				s.createdAt.goe(startDateTime),
				s.createdAt.lt(endDateTime),
				s.testCasePassedCount.eq(s.testCaseTotalCount)
			)
			.groupBy(s.user.id)
			.fetch();
	}
}
