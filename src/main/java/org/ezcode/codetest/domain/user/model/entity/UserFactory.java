package org.ezcode.codetest.domain.user.model.entity;

import java.util.UUID;

import org.ezcode.codetest.application.usermanagement.user.dto.response.OAuth2Response;

public class UserFactory {
    public static User createSocialUser(
        OAuth2Response response,
        String nickname,
        String provider
    ) {
        //나중에 확장성 고려해서 switch문 사용
        return switch (provider.toLowerCase()) {
            case "github" -> User.githubUser(
                response.getEmail(),
                response.getName(),
                nickname,
                UUID.randomUUID().toString(),
                response.getGithubUrl()
            );
            default -> User.socialUser(
                response.getEmail(),
                response.getName(),
                nickname,
                UUID.randomUUID().toString()
            );
        };
    }
}

