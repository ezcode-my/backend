package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import org.ezcode.codetest.application.problem.dto.request.ProblemSearchCondition;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.QProblem;
import org.ezcode.codetest.domain.problem.repository.ProblemRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProblemQueryRepositoryImpl implements ProblemRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<Problem> searchByCondition(Pageable pageable, ProblemSearchCondition searchCondition) {

		QProblem problem = QProblem.problem;
		BooleanBuilder builder = new BooleanBuilder();

		return null;
	}
}
