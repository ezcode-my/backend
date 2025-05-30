package org.ezcode.codetest.infrastructure.persitence.repository.user;

import java.util.Optional;

import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.repository.UserRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final UserJpaRepository userJpaRepository;

	@Override
	public void createUser(User user) {
		userJpaRepository.save(user);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return Optional.ofNullable(userJpaRepository.findUserByEmail(email));
	}

}
