package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.ProblemSearchCondition;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.QProblem;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProblemQueryRepositoryImpl implements ProblemRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<Problem> searchByCondition(Pageable pageable, ProblemSearchCondition searchCondition) {

		QProblem problem = QProblem.problem;

		// where 조건을 깔끔 하게 조립
		BooleanBuilder builder = new BooleanBuilder();

		builder.and(problem.isDeleted.isFalse());

		/*
		if(searchCondition.category() != null) {
			builder.and(problem.categories.contains(searchCondition.category()));
		}
		*/
		if (searchCondition.difficulty() != null) {
			builder.and(problem.difficulty.eq(Difficulty.valueOf(searchCondition.difficulty())));
		}

		List<Problem> content = jpaQueryFactory
			.selectFrom(problem)
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(problem.createdAt.desc())
			.fetch();

		Long total = jpaQueryFactory
			.select(problem.count())
			.from(problem)
			.where(builder)
			.fetchOne();

		return new PageImpl<>(content, pageable, total != null ? total : 0);
	}
}
