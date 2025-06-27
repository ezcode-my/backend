package org.ezcode.codetest.application.usermanagement.user.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "유저의 Public Repository 리스트를 보여줍니다")
public class UserGithubRepoResponse {
    private String repoName;
    private String defaultBranch;

    public UserGithubRepoResponse(String repoName, String defaultBranch) {
        this.repoName = repoName;
        this.defaultBranch = defaultBranch;
    }
}
