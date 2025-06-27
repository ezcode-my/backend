package org.ezcode.codetest.domain.user.repository;

import org.ezcode.codetest.domain.user.model.entity.UserGithubInfo;

public interface UserGithubInfoRepository {
    void createUserGithubInfo(UserGithubInfo userGithubInfo);

    UserGithubInfo getUserGithubInfo(Long id);

    void updateGithubAccessToken(UserGithubInfo userGithubInfo);
}
