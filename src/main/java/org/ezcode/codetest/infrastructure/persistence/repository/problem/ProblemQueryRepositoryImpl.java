package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.ProblemSearchCondition;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.QCategory;
import org.ezcode.codetest.domain.problem.model.entity.QProblem;
import org.ezcode.codetest.domain.problem.model.entity.QProblemCategory;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProblemQueryRepositoryImpl implements ProblemRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<Problem> searchByCondition(Pageable pageable, ProblemSearchCondition searchCondition) {

		QProblem problem = QProblem.problem;
		QProblemCategory problemCategory = QProblemCategory.problemCategory;
		QCategory category = QCategory.category;

		// where 조건을 깔끔 하게 조립
		BooleanBuilder builder = new BooleanBuilder();

		builder.and(problem.isDeleted.isFalse());

		// 카테고리 필터링
		if (searchCondition.category() != null) {
			builder.and(category.code.eq(searchCondition.category())
				.or(category.korName.eq(searchCondition.category())));
		}

		// 난이도 필터링
		if (searchCondition.difficulty() != null) {
			builder.and(problem.difficulty.eq(Difficulty.valueOf(searchCondition.difficulty())));
		}

		JPAQuery<Problem> query = jpaQueryFactory
			.selectDistinct(problem)
			.from(problem)
			.leftJoin(problemCategory).on(problem.eq(problemCategory.problem))
			.leftJoin(problemCategory.category, category)
			.where(builder);



		List<Problem> content = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(problem.createdAt.desc())
			.fetch();

		Long total = jpaQueryFactory
			.select(problem.countDistinct())
			.from(problem)
			.leftJoin(problemCategory).on(problem.eq(problemCategory.problem))
			.leftJoin(problemCategory.category, category)
			.where(builder)
			.fetchOne();

		return new PageImpl<>(content, pageable, total != null ? total : 0);
	}
}
