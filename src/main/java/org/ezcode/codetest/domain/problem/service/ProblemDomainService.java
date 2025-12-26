package org.ezcode.codetest.domain.problem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ezcode.codetest.domain.problem.model.ProblemSearchCondition;
import org.ezcode.codetest.domain.problem.exception.ProblemException;
import org.ezcode.codetest.domain.problem.exception.code.ProblemExceptionCode;
import org.ezcode.codetest.domain.problem.model.ProblemInfo;
import org.ezcode.codetest.domain.problem.model.entity.Category;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.ProblemCategory;
import org.ezcode.codetest.domain.problem.repository.CategoryRepository;
import org.ezcode.codetest.domain.problem.repository.ProblemCategoryRepository;
import org.ezcode.codetest.domain.problem.repository.ProblemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemDomainService {

	private final ProblemRepository problemRepository;
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

	public void updateCategoryAndSearchEngine(Problem problem, Map<String, String> categories) {

		problemCategoryRepository.deleteAllByProblemId(problem.getId());

		List<String> codes = new ArrayList<>(categories.keySet());
		List<Category> categoryList = categoryRepository.findAllByCategoryCodeIn(codes);

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
			.map(cat -> ProblemCategory.from(savedProblem, cat))
			.toList();

		problemCategoryRepository.saveAll(problemCategories);

		return savedProblem;
	}

	public Set<String> getSearchKeywordSuggestions(String keyword) {

		return problemRepository.findAutoComplete(keyword);
	}

	public Page<Problem> getProblemBySearchCondition(Pageable pageable, ProblemSearchCondition searchCondition) {

		return problemRepository.searchByCondition(pageable, searchCondition);
	}

	public Problem getProblem(Long problemId) {

		return problemRepository.findProblemWithTestcasesById(problemId)
			.orElseThrow(() -> new ProblemException(ProblemExceptionCode.PROBLEM_NOT_FOUND));
	}

	public void removeProblem(Problem problem) {

		problemRepository.delete(problem);
	}

	public ProblemInfo getProblemInfo(Long problemId) {
		Problem problem = problemRepository.findProblemWithTestcasesById(problemId)
			.orElseThrow(() -> new ProblemException(ProblemExceptionCode.PROBLEM_NOT_FOUND));

		List<ProblemCategory> categories = problemCategoryRepository.findByProblemId(problemId);

		List<String> categoryNames = categories.stream().map(cat -> cat.getCategory().getKorName()).toList();

		return new ProblemInfo(problem, problem.getTestcases(), categoryNames);
	}

	// 문제 수정시 문제를 DB 저장 용도
	public void saveProblem(Problem problem) {
		problemRepository.save(problem);
	}

	public void problemCountAdjustment(Long problemId, int correctInc) {
		problemRepository.problemCountAdjustment(problemId, correctInc);
	}

    public List<Long> getProblemIdList() {
		return problemRepository.getProblemIdList();
    }
}
