package org.ezcode.codetest.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.user.model.entity.User;

public interface UserRepository {
	void createUser(User user);

	Optional<User> findByEmail(String email);

	User getUserByEmail(String email);

	Optional<User> findUserById(Long id);

	boolean existsByNickname(String nickname);

	void decreaseReviewToken(User user);

	void updateReviewTokens(List<Long> ids , int newToken);
}
