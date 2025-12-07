package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProblemQueryRepositoryImpl implements ProblemRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	QProblem problem = QProblem.problem;
	QCategory category = QCategory.category;
	QProblemCategory problemCategory = QProblemCategory.problemCategory;

	@Override
	public Set<String> findAutoComplete(String keyword) {
		if (keyword == null || keyword.isBlank()) {
			return Collections.emptySet();
		}

		// 1. 카테고리 검색 (Category korName)
		List<String> categories = jpaQueryFactory
			.select(category.korName)
			.from(category)
			.where(category.korName.containsIgnoreCase(keyword))
			.limit(5) // 카테고리는 최대 5개까지만 노출
			.fetch();

		// 2. 문제 검색 (Title, Description 대상)
		// 설명(description)에 키워드가 있어도, 자동완성 목록에는 '제목(title)'을 보여주는 것이 일반적입니다.
		List<String> problems = jpaQueryFactory
			.select(problem.title)
			.from(problem)
			.where(
				problem.title.containsIgnoreCase(keyword)
					.or(problem.description.containsIgnoreCase(keyword))
			)
			.limit(10) // 문제는 최대 10개까지만 노출
			.fetch();

		// 3. 결과 합치기 (LinkedHashSet: 입력 순서 보장 + 중복 제거)
		// 카테고리를 먼저 add 해서 상단에 노출되도록 함
		Set<String> result = new LinkedHashSet<>();
		result.addAll(categories);
		result.addAll(problems);

		return result;
	}

	@Override
	public Page<Problem> searchByCondition(Pageable pageable, ProblemSearchCondition searchCondition) {

		BooleanBuilder booleanBuilder = new BooleanBuilder();

		booleanBuilder.and(eqDifficulty(searchCondition.difficulty()));
		booleanBuilder.and(eqCategoryCode(searchCondition.categoryCode()));
		booleanBuilder.and(containsKeyword(searchCondition.keyword()));
		booleanBuilder.and(problem.isDeleted.isFalse());

		JPAQuery<Problem> query = jpaQueryFactory
			.selectDistinct(problem)
			.from(problem)
			.leftJoin(problemCategory).on(problem.eq(problemCategory.problem))
			.leftJoin(problemCategory.category, category)
			.where(booleanBuilder);

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
			.where(booleanBuilder)
			.fetchOne();

		return new PageImpl<>(content, pageable, total != null ? total : 0);
	}

	private BooleanExpression eqDifficulty(Difficulty difficulty) {

		return difficulty != null ? problem.difficulty.eq(difficulty) : null;
	}

	private BooleanExpression eqCategoryCode(String categoryCode) {

		return categoryCode != null ? category.code.eq(categoryCode) : null;
	}

	private BooleanExpression containsKeyword(String keyword) {

		if (keyword == null) {
			return null;
		}

		return problem.title.containsIgnoreCase(keyword)
			.or(problem.description.containsIgnoreCase(keyword))
			.or(category.korName.containsIgnoreCase(keyword));
	}
}
