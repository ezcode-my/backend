package org.ezcode.codetest.domain.user.service;

import org.ezcode.codetest.domain.user.model.entity.UserAuthType;
import org.ezcode.codetest.domain.user.model.enums.Adjective;
import org.ezcode.codetest.domain.user.model.enums.AuthType;
import org.ezcode.codetest.domain.user.model.enums.Noun;
import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.exception.AuthExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.repository.UserAuthTypeRepository;
import org.ezcode.codetest.domain.user.repository.UserRepository;
import org.ezcode.codetest.common.security.util.PasswordEncoder;
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
	private final UserAuthTypeRepository userAuthTypeRepository;
	private final PasswordEncoder passwordEncoder;
	private static final java.util.Random RANDOM = new java.util.Random();

	public void checkEmailUnique(String email) {
		if (userRepository.findByEmail(email).isPresent()) {
			throw new AuthException(AuthExceptionCode.ALREADY_EXIST_USER);
		}
	}

	public void createUser(User user) {
		userRepository.createUser(user);
	}

	public void createUserAuthType(UserAuthType userAuthType) {
		userAuthTypeRepository.createUserAuthType(userAuthType);
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

	public void passwordComparison(String newPassword, String oldPassword) {
		if (passwordEncoder.matches(newPassword, oldPassword)) {
			throw new AuthException(AuthExceptionCode.PASSWORD_IS_SAME);
		}
	}

	public void isDeletedUser(User user) {
		if (user.isDeleted()) {
			throw new AuthException(AuthExceptionCode.ALREADY_WITHDRAW_USER);
		}
	}

	public String generateUniqueNickname() {
		for (int i = 0; i < 10000000; i++) {
			String nickname = generateRandomNickname();
			if(!userRepository.existsByNickname(nickname)) {
				return nickname;
			}
		}
		throw new IllegalStateException("중복된 닉네임 생성 불가");
	}

	private static String generateRandomNickname() {
		Adjective adjective = Adjective.values()[RANDOM.nextInt(Adjective.values().length)];
		Noun noun = Noun.values()[RANDOM.nextInt(Noun.values().length)];
		int number = RANDOM.nextInt(1000);
		return adjective.name() + noun.name() + number;
	}


}
