package org.ezcode.codetest.infrastructure.persistence.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
	User findUserByEmail(String email);

	boolean existsByNickname(String nickname);
}
