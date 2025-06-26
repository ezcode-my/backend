package org.ezcode.codetest.application.problem.dto.request;

import org.ezcode.codetest.domain.problem.model.entity.Category;

public record CategoryCreateRequest(

	String categoryCode,

	String categoryName

) {
	public Category toCategory() {

		return new Category(categoryCode, categoryName);
	}
}
