package org.ezcode.codetest.domain.problem.repository;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.entity.Category;

public interface CategoryRepository {

	Category save(Category category);

	Optional<Category> findByCategoryCode(String categoryCode);

	List<Category> findAllByCategoryCodeIn(List<String> categoryCodes);
}
