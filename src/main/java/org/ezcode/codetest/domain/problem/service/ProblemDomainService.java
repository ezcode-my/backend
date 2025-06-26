package org.ezcode.codetest.domain.problem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ezcode.codetest.domain.problem.model.ProblemSearchCondition;
import org.ezcode.codetest.domain.problem.exception.ProblemException;
import org.ezcode.codetest.domain.problem.exception.code.ProblemExceptionCode;
import org.ezcode.codetest.domain.problem.model.ProblemInfo;
import org.ezcode.codetest.domain.problem.model.entity.Category;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.ProblemCategory;
import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;
import org.ezcode.codetest.domain.problem.repository.CategoryRepository;
import org.ezcode.codetest.domain.problem.repository.ProblemCategoryRepository;
import org.ezcode.codetest.domain.problem.repository.ProblemDocumentRepository;
import org.ezcode.codetest.domain.problem.repository.ProblemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemDomainService {

	private final ProblemRepository problemRepository;
	private final ProblemDocumentRepository searchRepository;
	private final CategoryRepository categoryRepository;
	private final ProblemCategoryRepository problemCategoryRepository;

	public Category createCategory(Category category) {

		return categoryRepository.save(category);
	}

	public List<Category> getProblemCategoryList(Problem problem) {

		List<ProblemCategory> problemCategories = problemCategoryRepository.findByProblemId(problem.getId());

		return problemCategories.stream().map(ProblemCategory::getCategory).toList();
	}

	public List<ProblemCategory> getProblemsCategoryList(List<Problem> problem) {

		List<Long> problemIds = problem.stream().map(Problem::getId).toList();

		return problemCategoryRepository.findByProblemIdsIn(problemIds);
	}

	public void updateProblemCategory(Problem problem, List<String> categories) {

		problemCategoryRepository.deleteAllByProblemId(problem.getId());

		List<Category> categoryList = categoryRepository.findAllByCategoryCodeIn(categories);

		List<ProblemCategory> problemCategories = categoryList.stream()
			.map(cat -> ProblemCategory.from(problem, cat))
			.toList();

		problemCategoryRepository.saveAll(problemCategories);
	}

	public Problem createProblem(Problem problem, Map<String, String> categories) {

		if (problemRepository.existsByTitleAndIsDeletedIsFalse(problem.getTitle())) {
			throw new ProblemException(ProblemExceptionCode.DUPLICATE_PROBLEM);
		}

		Problem savedProblem = problemRepository.save(problem);

		List<String> codes = new ArrayList<>(categories.keySet());

		List<Category> categoryList = categoryRepository.findAllByCategoryCodeIn(codes);

		List<ProblemCategory> problemCategories = categoryList.stream()
			.map(cat -> ProblemCategory.from(problem, cat))
			.toList();

		problemCategoryRepository.saveAll(problemCategories);

		searchRepository.save(ProblemSearchDocument.from(savedProblem, categoryList));

		return savedProblem;
	}

	public Page<Problem> getProblemBySearchCondition(Pageable pageable, ProblemSearchCondition searchCondition) {

		return problemRepository.searchByCondition(pageable, searchCondition);
	}

	public Problem getProblem(Long problemId) {

		return problemRepository.findByIdNotDeleted(problemId)
			.orElseThrow(() -> new ProblemException(ProblemExceptionCode.PROBLEM_NOT_FOUND));
	}

	public void removeProblem(Problem problem) {

		problemRepository.delete(problem);

		ProblemSearchDocument document = searchRepository.findById(problem.getId())
			.orElseThrow(() -> new ProblemException(ProblemExceptionCode.PROBLEM_NOT_FOUND));

		searchRepository.delete(document);
	}

	public ProblemInfo getProblemInfo(Long problemId) {
		Problem problem = problemRepository.findProblemWithTestcasesById(problemId)
			.orElseThrow(() -> new ProblemException(ProblemExceptionCode.PROBLEM_NOT_FOUND));

		return new ProblemInfo(problem, problem.getTestcases());
	}

}
