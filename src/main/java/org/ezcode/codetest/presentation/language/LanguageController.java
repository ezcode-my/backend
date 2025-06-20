package org.ezcode.codetest.presentation.language;

import java.util.List;

import org.ezcode.codetest.application.submission.dto.request.language.LanguageCreateRequest;
import org.ezcode.codetest.application.submission.dto.response.language.LanguageResponse;
import org.ezcode.codetest.application.submission.service.LanguageService;
import org.ezcode.codetest.application.submission.dto.request.language.LanguageUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/languages")
@Tag(name = "Language", description = "프로그래밍 언어 관리 API")
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping
    @Operation(summary = "언어 생성", description = "새로운 프로그래밍 언어를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "언어 생성 성공")
    public ResponseEntity<LanguageResponse> createLanguage(
        @RequestBody @Valid LanguageCreateRequest request
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(languageService.createLanguage(request));
    }

    @GetMapping
    @Operation(summary = "언어 목록 조회", description = "등록된 모든 프로그래밍 언어 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "언어 목록 조회 성공")
    public ResponseEntity<List<LanguageResponse>> getLanguages() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(languageService.getLanguages());
    }

    @PutMapping("/{languageId}")
    @Operation(summary = "언어 수정", description = "기존 프로그래밍 언어 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "언어 수정 성공")
    public ResponseEntity<LanguageResponse> modifyLanguage(
        @Parameter(description = "수정할 언어 ID", required = true)
        @PathVariable Long languageId,
        @RequestBody @Valid LanguageUpdateRequest request
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(languageService.modifyLanguage(languageId, request));
    }

    @DeleteMapping("/{languageId}")
    @Operation(summary = "언어 삭제", description = "등록된 프로그래밍 언어를 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "언어 삭제 성공")
    public ResponseEntity<Void> removeLanguage(
        @Parameter(description = "삭제할 언어 ID", required = true)
        @PathVariable Long languageId
    ) {
        languageService.removeLanguage(languageId);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }
}
