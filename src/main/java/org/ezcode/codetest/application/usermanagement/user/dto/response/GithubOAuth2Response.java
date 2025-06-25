package org.ezcode.codetest.application.usermanagement.user.dto.response;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Github OAuth2 응답 처리 클래스")
public class GithubOAuth2Response implements OAuth2Response{
    private final Map<String, Object> attributes;

    public GithubOAuth2Response(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    @Schema(description = "OAuth 제공자 이름", example = "github")
    public String getProvider() {
        return "github";
    }

    @Override
    @Schema(description = "제공자 내부 식별자", example = "109000123456789012345")
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    @Schema(description = "사용자 이메일", example = "user@gmail.com")
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    @Schema(description = "사용자 이름", example = "홍길동")
    public String getName() {
        return attributes.get("name").toString();
    }
}
