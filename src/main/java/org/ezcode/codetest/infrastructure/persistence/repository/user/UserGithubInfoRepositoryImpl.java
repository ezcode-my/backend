package org.ezcode.codetest.infrastructure.persistence.repository.user;

import org.ezcode.codetest.domain.user.model.entity.UserGithubInfo;
import org.ezcode.codetest.domain.user.repository.UserGithubInfoRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserGithubInfoRepositoryImpl implements UserGithubInfoRepository {
    private final UserGithubInfoJpaRepository userGithubInfoJpaRepository;

    @Override
    public void createUserGithubInfo(UserGithubInfo userGithubInfo) {
        userGithubInfoJpaRepository.save(userGithubInfo);
    }

    @Override
    public UserGithubInfo getUserGithubInfo(Long userId) {
        return userGithubInfoJpaRepository.findUserGithubInfoByUser_Id(userId);
    }

    @Override
    public void updateGithubAccessToken(UserGithubInfo userGithubInfo) {
        userGithubInfoJpaRepository.save(userGithubInfo);
    }

    @Override
    public void updateGithubInfo(UserGithubInfo userGithub) {
        userGithubInfoJpaRepository.save(userGithub);
    }
}
