package org.ezcode.codetest.infrastructure.persistence.repository.user;

import org.ezcode.codetest.domain.user.model.enums.AuthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
	User findUserByEmail(String email);

	User findUserByEmailAndAuthType(String email, AuthType authType);

	boolean existsByNickname(String nickname);

	Optional<User> findById(Long id);

}
