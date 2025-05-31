package org.ezcode.codetest.domain.user.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.stereotype.Repository;

public interface UserRepository {
	void createUser(User user);

	Optional<User> findByEmail(String email);
}
