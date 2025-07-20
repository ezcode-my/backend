package org.ezcode.codetest.presentation.problemmanagement.problem;

import org.ezcode.codetest.application.problem.dto.request.CategoryCreateRequest;
import org.ezcode.codetest.application.problem.dto.request.ProblemCreateRequest;
import org.ezcode.codetest.application.problem.dto.request.ProblemUpdateRequest;
import org.ezcode.codetest.application.problem.service.ProblemService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/problems")
@RequiredArgsConstructor
@Tag(name = "Problem(관리자)", description = "문제 API(관리자용)")
public class ProblemAdminController {

	private final ProblemService problemService;

	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@Operation(summary = "문제 등록", description = "문제를 등록합니다.")
	@ApiResponse(responseCode = "201", description = "문제 생성 성공")
	public ResponseEntity<Void> createProblem(
		@RequestPart @Valid ProblemCreateRequest request,
		@RequestPart(value = "image", required = false) MultipartFile image,
		@AuthenticationPrincipal AuthUser user
	) {
		problemService.createProblem(request, image, user);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.build();
	}

	@PutMapping(path = "/{problemId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@Operation(summary = "문제 전체 수정", description = "문제 전체를 수정합니다.")
	@ApiResponse(responseCode = "200", description = "문제 수정 성공")
	public ResponseEntity<Void> modifyProblem(
		@PathVariable Long problemId,
		@RequestPart @Valid ProblemUpdateRequest request,
		@RequestPart(value = "image", required = false) MultipartFile image
	) {
		problemService.modifyProblem(problemId, request, image);

		return ResponseEntity
			.status(HttpStatus.OK)
			.build();
	}

	@PutMapping("/image/{problemId}")
	@Operation(summary = "문제 이미지 수정", description = "문제 이미지 수정합니다.")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiResponse(responseCode = "200", description = "문제 수정 성공")
	public ResponseEntity<Void> updateProblemImage(
		@PathVariable Long problemId,
		@RequestPart(value = "image", required = false) MultipartFile imageFile
	) {
		problemService.addImageToExistingProblem(problemId, imageFile);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{problemId}")
	@Operation(summary = "문제 삭제", description = "문제를 삭제합니다.")
	@ApiResponse(responseCode = "204", description = "문제 삭제 성공")
	public ResponseEntity<Void> removeProblem(@PathVariable Long problemId) {

		problemService.removeProblem(problemId);

		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}

	@PostMapping("/categories")
	public ResponseEntity<Void> createCategory(@RequestBody CategoryCreateRequest request) {

		problemService.createCategory(request);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.build();
	}

}
