package org.ezcode.codetest.presentation.usermanagement;

import java.util.List;

import org.ezcode.codetest.application.usermanagement.user.dto.request.UserGithubRepoSelectRequest;
import org.ezcode.codetest.application.usermanagement.user.dto.response.UserGithubRepoResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.UserGitubAutoPushResponse;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.service.UserGithubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "깃허브 자동 push 관련 api", description = "자동 Push할 Repository 선택, 자동 Push 여부 선택")
public class UserGithubController {

    private final UserGithubService userGithubService;

    @Operation(summary = "GitHub 레포지토리 목록 조회", description = "사용자의 GitHub 레포지토리 목록을 조회합니다")
    @GetMapping("/users/github")
    public ResponseEntity<List<UserGithubRepoResponse>> getGithubRepos(
        @AuthenticationPrincipal AuthUser authUser
    ) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(userGithubService.getGithubRepos(authUser));
    }

    @Operation(summary = "Repository 선택", description = "사용자의 Repository 중, 자동 push를 원하는 repo를 선택합니다")
    @PostMapping("/users/github")
    public ResponseEntity<UserGithubRepoResponse> selectGithubRepo(
        @AuthenticationPrincipal AuthUser authUser,
        @Valid @RequestBody UserGithubRepoSelectRequest reqeust
    ) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(userGithubService.selectGithubRepo(authUser, reqeust));
    }

    @Operation(summary = "깃허브 자동 push 기능 여부 선택", description = "깃허브 자동 push 기능 여부를 킬건지 말건지 선택합니다.(true/false)")
    @PutMapping("/users/github")
    public ResponseEntity<UserGitubAutoPushResponse> changeAutoPushSetting(
        @AuthenticationPrincipal AuthUser authUser
    ){
        return ResponseEntity.status(HttpStatus.OK).body(userGithubService.changeAutoPushSetting(authUser));
    }
}
