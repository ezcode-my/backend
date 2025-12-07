package org.ezcode.codetest.application.problem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.ezcode.codetest.application.problem.dto.request.CategoryCreateRequest;
import org.ezcode.codetest.application.problem.dto.request.ProblemCreateRequest;
import org.ezcode.codetest.application.problem.dto.request.ProblemUpdateRequest;
import org.ezcode.codetest.application.problem.dto.response.ProblemDetailResponse;
import org.ezcode.codetest.application.problem.dto.response.ProblemResponse;
import org.ezcode.codetest.domain.game.model.character.CategoryStat;
import org.ezcode.codetest.domain.game.util.StatUpdateUtil;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.ProblemSearchCondition;
import org.ezcode.codetest.domain.problem.model.entity.Category;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.ProblemCategory;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
import org.ezcode.codetest.domain.problem.model.enums.Reference;
import org.ezcode.codetest.domain.problem.service.ProblemDomainService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.ezcode.codetest.infrastructure.s3.S3Uploader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ProblemServiceTest {

	@Mock
	private ProblemDomainService problemDomainService;

	@Mock
	private UserDomainService userDomainService;

	@Mock
	private StatUpdateUtil statUpdateUtil;

	@Mock
	private S3Uploader s3Uploader;

	@InjectMocks
	private ProblemService problemService;


	@Test
	@DisplayName("카테고리 생성")
	void createCategory() {

		// given
		CategoryCreateRequest createRequest = new CategoryCreateRequest("MATH", "수학");
		Category category = new Category("MATH", "수학");

		when(problemDomainService.createCategory(any())).thenReturn(category);

		// when
		problemService.createCategory(createRequest);

		// then
		verify(problemDomainService).createCategory(any());
		verify(statUpdateUtil).save(any(CategoryStat.class));
	}

	@Test
	@DisplayName("문제 생성 요청 시 유저 조회 및 문제 도메인 저장")
	void createProblem_shouldCreateWithoutImage() {
		Language language = new Language("java", "17", 30L);
		// given
		AuthUser auth = new AuthUser(1L, "test", "test", "test@test", UserRole.ADMIN, Tier.NEWBIE);
		User user = new User("test@test", "test", "test1", "test1", 13,
			Tier.NEWBIE, UserRole.ADMIN, false, false, "testurl", false, language);

		ProblemCreateRequest request = new ProblemCreateRequest(
			Map.of("Math", "수학"),
			"A+B",
			"두 수를 더하라",
			Difficulty.LV1,
			1000L,
			200L,
			Reference.ORIGINAL
		);

		Problem problem = mock(Problem.class);
		Problem saved = mock(Problem.class);

		// static 메서드 mocking
		try (MockedStatic<ProblemCreateRequest> mockedStatic = mockStatic(ProblemCreateRequest.class)) {
			mockedStatic.when(() -> ProblemCreateRequest.toProblem(request, user)).thenReturn(problem);

			when(userDomainService.getUserById(1L)).thenReturn(user);
			when(problemDomainService.createProblem(problem, request.categories())).thenReturn(saved);

			// when
			problemService.createProblem(request, null, auth);

			// then
			verify(userDomainService).getUserById(1L);
			verify(problemDomainService).createProblem(problem, request.categories());
			verify(s3Uploader, never()).upload(any(), any());
		}
	}

	// @Test
	// @DisplayName("문제 목록 조회 시 카테고리 매핑 포함")
	// void getProblemWithCondition_shouldReturnMappedResponses() {
	// 	// given
	// 	Pageable pageable = PageRequest.of(0, 5);
	// 	Problem p1 = mock(Problem.class);
	// 	Category c1 = new Category("DS", "자료구조");
	//
	// 	when(problemDomainService.getProblemBySearchCondition(eq(pageable), any())).thenReturn(new PageImpl<>(List.of(p1)));
	// 	when(problemDomainService.getProblemsCategoryList(List.of(p1)))
	// 		.thenReturn(List.of(ProblemCategory.from(p1, c1)));
	//
	// 	// when
	// 	Page<ProblemResponse> result = problemService.getProblemWithCondition(pageable, new ProblemSearchCondition("MATH", "LV1"));
	//
	// 	// then
	// 	assertEquals(1, result.getContent().size());
	// 	verify(problemDomainService).getProblemsCategoryList(List.of(p1));
	// }

	@Test
	@DisplayName("문제 상세 조회")
	void getProblem() {

		// given
		Long problemId = 1L;
		User user = mock(User.class);
		Problem problem = new Problem(user, "제목", "설명", Difficulty.LV1.getScore(), Difficulty.LV1, 1000L, 256L, Reference.ORIGINAL);
		List<Category> categories = List.of(new Category("MATH", "수학"));

		when(problemDomainService.getProblem(problemId)).thenReturn(problem);
		when(problemDomainService.getProblemCategoryList(problem)).thenReturn(categories);
		// when
		ProblemDetailResponse response = problemService.getProblem(1L);

		// then
		assertNotNull(response);
		assertEquals(problem.getId(), response.id());
		assertEquals(1, response.categories().size());
		assertEquals("수학", response.categories().get(0));

		verify(problemDomainService).getProblem(problemId);
		verify(problemDomainService).getProblemCategoryList(problem);
	}

	@Test
	@DisplayName("문제 수정")
	void modifyProblem() {

		// given
		Problem problem = mock(Problem.class);
		ProblemUpdateRequest updateRequest = mock(ProblemUpdateRequest.class);

		when(problemDomainService.getProblem(1L)).thenReturn(problem);
		when(updateRequest.title()).thenReturn("새 제목");
		when(updateRequest.categories()).thenReturn(List.of("MATH"));
		// when
		problemService.modifyProblem(1L, updateRequest, null);

		// then
		verify(problem).update(any(), any(), any(), any(), any(), any(), any());
		verify(problemDomainService).updateCategoryAndSearchEngine(problem, List.of("MATH"));
	}

	@Test
	@DisplayName("문제 삭제")
	void removeProblem() {

		// given
		Problem problem = mock(Problem.class);
		when(problem.getImageUrl()).thenReturn(List.of("url1"));
		when(problemDomainService.getProblem(1L)).thenReturn(problem);

		// when
		problemService.removeProblem(1L);

		// then
		verify(s3Uploader, times(1)).delete(anyString(), eq("problem"));
		verify(problem).clearImages();
		verify(problemDomainService).saveProblem(problem);
		verify(problemDomainService).removeProblem(problem);

	}

	@Test
	@DisplayName("문제 이미지 단건 추가")
	void updateProblemWithImage_shouldAddUrl() {
		Problem p = mock(Problem.class);
		when(problemDomainService.getProblem(1L)).thenReturn(p);

		problemService.updateProblemWithImage(1L, "img-url");

		verify(p).addImage("img-url");
	}
}