package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.entity.Category;
import org.ezcode.codetest.domain.problem.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

	private final CategoryJpaRepository categoryRepository;

	@Override
	public Category save(Category category) {

		return categoryRepository.save(category);
	}

	@Override
	public Optional<Category> findByCategoryCode(String categoryCode) {

		return categoryRepository.findByCode(categoryCode);
	}

	@Override
	public List<Category> findAllByCategoryCodeIn(List<String> categoryCodes) {

		return categoryRepository.findAllByCodeIn(categoryCodes);
	}

}
