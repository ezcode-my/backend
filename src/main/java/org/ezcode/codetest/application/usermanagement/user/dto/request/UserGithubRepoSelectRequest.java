package org.ezcode.codetest.application.usermanagement.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "깃허브 repository 선택 요청")
public record UserGithubRepoSelectRequest(
    @Schema(description = "repository 이름", example = "Practice coding")
    String repositoryName
) {}