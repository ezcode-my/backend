package org.ezcode.codetest.domain.user.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Repository;

public interface UserRepository {
	void createUser(User user);

	Optional<User> findByEmail(String email);

	User getUserByEmail(String email);

	Optional<User> findUserById(Long id);

	User findByEmailAndProvider(String email, String provider);

	boolean existsByNickname(String nickname);

}
