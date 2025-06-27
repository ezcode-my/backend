package org.ezcode.codetest.infrastructure.persistence.repository.user;

import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.entity.UserGithubInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGithubInfoJpaRepository extends JpaRepository<UserGithubInfo, Long> {
    UserGithubInfo findUserGithubInfoByUser(User user);

    UserGithubInfo findUserGithubInfoByUser_Id(Long userId);
}
