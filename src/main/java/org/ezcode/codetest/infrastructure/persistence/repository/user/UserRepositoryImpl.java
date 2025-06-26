package org.ezcode.codetest.infrastructure.persistence.repository.user;

import java.util.List;
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

	public User getUserByEmail(String email) {
		return userJpaRepository.findUserByEmail(email);
	}

	public Optional<User> findUserById(Long id) {
		return userJpaRepository.findById(id);
	}

	@Override
	public boolean existsByNickname(String nickname) {
		return userJpaRepository.existsByNickname(nickname);
	}

	@Override
	public void decreaseReviewToken(User user) {
		user.decreaseReviewToken();
	}

	@Override
	public void updateReviewTokens(List<Long> ids, int newToken) {
		userJpaRepository.updateReviewTokens(ids, newToken);
	}

	@Override
	public void updateUserGithubAccessToken(User loginUser) {
		userJpaRepository.save(loginUser);
	}

}
