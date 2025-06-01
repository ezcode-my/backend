package org.ezcode.codetest.domain.user.service;

import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.repository.UserRepository;
import org.ezcode.codetest.infrastructure.security.jwt.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDomainService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public boolean existUser(String email) {
		return userRepository.findByEmail(email).isPresent();
	}

	public void createUser(User user) {
		userRepository.createUser(user);
	}

	public User findUser(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new AuthException("사용자를 찾을 수 없습니다."));
	}

	public User findUserById(Long id) {
		return userRepository.findUserById(id)
			.orElseThrow(()->new AuthException("사용자를 찾을 수 없습니다."));
	}

	public void userPasswordCheck(String email, String password) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new AuthException("사용자를 찾을 수 없습니다."));;

		if (passwordEncoder.matches(password, user.getPassword())) {
			throw new AuthException("비밀번호가 일치하지 않습니다");
		}
	}

	public String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

}
