package org.ezcode.codetest.domain.user.service;

import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.exception.AuthExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.repository.UserRepository;
import org.ezcode.codetest.infrastructure.security.jwt.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserDomainService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public void checkEmailUnique(String email) {
		if (userRepository.findByEmail(email).isPresent()) {
			throw new AuthException(AuthExceptionCode.EXIST_USER_EMAIL);
		}
	}

	public void createUser(User user) {
		userRepository.createUser(user);
	}

	public User getUser(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new AuthException(AuthExceptionCode.USER_NOT_FOUND));
	}

	public User getUserById(Long id) {
		return userRepository.findUserById(id)
			.orElseThrow(()->new AuthException(AuthExceptionCode.USER_NOT_FOUND));
	}

	public void userPasswordCheck(String email, String password) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new AuthException(AuthExceptionCode.USER_NOT_FOUND));;

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new AuthException(AuthExceptionCode.PASSWORD_NOT_MATCH);
		}
	}

	public String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	public User getOAuthUser(String email, String provider) {
		return userRepository.findByEmailAndProvider(email, provider);
	}
}
