package org.ezcode.codetest.application.usermanagement.user.dto.response;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        if (attributes.get("email") == null && getProvider().equals("github")) {
            return getGithubId()+"@github.com";
        } else {
            return (String) attributes.get("email");
        }
    }

    @Override
    @Schema(description = "사용자 이름", example = "홍길동")
    public String getName() {
        return Optional.ofNullable(attributes.get("name"))
            .map(Object::toString)
            .orElseGet(()-> Optional.ofNullable(attributes.get("login"))
                .map(Object::toString)
            .orElse("Github_"+UUID.randomUUID()));
    }

    @Override
    @Schema(description = "깃허브 고유 Id", example = "1345932")
    public String getGithubId() {
        return attributes.get("id").toString();
    }

    @Override
    @Schema(description = "깃허브 홈 URL", example = "https://github.com/id")
    public String getGithubUrl(){
        return attributes.get("html_url").toString();
    }

    @Override
    @Schema(description = "깃허브 아이디(login name)", example = "ezcode")
    public String getOwner(){
        return attributes.get("login").toString();
    }
}
