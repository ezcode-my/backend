package org.ezcode.codetest.domain.problem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.entity.Category;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.ProblemCategory;
import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
import org.ezcode.codetest.domain.problem.model.enums.Reference;
import org.ezcode.codetest.domain.problem.repository.CategoryRepository;
import org.ezcode.codetest.domain.problem.repository.ProblemCategoryRepository;
import org.ezcode.codetest.domain.problem.repository.ProblemDocumentRepository;
import org.ezcode.codetest.domain.problem.repository.ProblemRepository;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProblemDomainServiceTest {

	@Mock
	private ProblemRepository problemRepository;
	@Mock private ProblemDocumentRepository searchRepository;
	@Mock private CategoryRepository categoryRepository;
	@Mock private ProblemCategoryRepository problemCategoryRepository;

	@InjectMocks
	private ProblemDomainService problemDomainService;

	@Test
	@DisplayName("문제 생성 성공")
	void createProblem_success() {

		// given
		User mockUser = mock(User.class);

		Problem problem = new Problem(mockUser, "A+B", "두 수를 더하라", 10, Difficulty.LV1, 1000L, 200L, Reference.ORIGINAL);
		Map<String, String> categoryMap = Map.of("ALGO", "알고리즘");

		when(problemRepository.existsByTitleAndIsDeletedIsFalse("A+B")).thenReturn(false);
		when(problemRepository.save(problem)).thenReturn(problem);

		Category category = new Category("ALGO", "알고리즘");
		when(categoryRepository.findAllByCategoryCodeIn(List.of("ALGO"))).thenReturn(List.of(category));

		ProblemCategory pc = ProblemCategory.from(problem, category);
		when(problemCategoryRepository.saveAll(anyList())).thenReturn(List.of(pc));

		ProblemSearchDocument doc = ProblemSearchDocument.from(problem, List.of(category));
		when(searchRepository.save(any(ProblemSearchDocument.class))).thenReturn(doc);

		// when
		Problem result = problemDomainService.createProblem(problem, categoryMap);

		// then
		assertNotNull(result);
		assertEquals("A+B", result.getTitle());
		verify(problemRepository).save(problem);
	}

	@Test
	@DisplayName("문제 저장 성공")
	void saveProblem_success() {

		// given
		Problem problem = mock(Problem.class);

		// when
		problemDomainService.saveProblem(problem);

		// then
		verify(problemRepository).save(problem);
	}

	@Test
	@DisplayName("문제 조회 성공")
	void getProblem_success() {

		// given
		Long problemId = 1L;
		Problem problem = mock(Problem.class);

		when(problemRepository.findByIdNotDeleted(problemId)).thenReturn(Optional.ofNullable(problem));

		// when
		Problem result = problemDomainService.getProblem(problemId);

		// then
		assertNotNull(result);
		assertEquals(problem, result);
		verify(problemRepository).findByIdNotDeleted(problemId);
	}

	@Test
	@DisplayName("문제 삭제 성공")
	void removeProblem_success() {

		// given
		Problem problem = mock(Problem.class);
		when(problem.getId()).thenReturn(1L);

		ProblemSearchDocument searchDocument = mock(ProblemSearchDocument.class);
		when(searchRepository.findById(1L)).thenReturn(Optional.of(searchDocument));

		// when
		problemDomainService.removeProblem(problem);

		// then
		verify(problemRepository).delete(problem);
		verify(searchRepository).findById(1L);
		verify(searchRepository).delete(searchDocument);
	}



	@Test
	@DisplayName("카테고리 생성 성공")
	void createCategory_success() {

		// given
		Category category = new Category("MATH", "수학");
		when(categoryRepository.save(category)).thenReturn(category);

		// when
		Category result = problemDomainService.createCategory(category);

		// then
		assertEquals("MATH", result.getCode());
		verify(categoryRepository).save(category);
	}

	@Test
	@DisplayName("문제 ID로 카테고리 리스트 조회 성공")
	void getProblemCategoryList_success() {

		// given
		Problem problem = mock(Problem.class);
		when(problem.getId()).thenReturn(1L);

		Category category = new Category("ALGO", "알고리즘");
		ProblemCategory pc = ProblemCategory.from(problem, category);

		when(problemCategoryRepository.findByProblemId(1L)).thenReturn(List.of(pc));

		// when
		List<Category> result = problemDomainService.getProblemCategoryList(problem);

		// then
		assertEquals(1, result.size());
		assertEquals("ALGO", result.get(0).getCode());
	}

}
