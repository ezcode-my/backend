package org.ezcode.codetest.domain.user.service;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.application.usermanagement.user.model.UsersByWeek;
import org.ezcode.codetest.domain.user.exception.UserException;
import org.ezcode.codetest.domain.user.exception.code.UserExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.UserAuthType;
import org.ezcode.codetest.domain.user.model.entity.UserGithubInfo;
import org.ezcode.codetest.domain.user.model.enums.Adjective;
import org.ezcode.codetest.domain.user.model.enums.AuthType;
import org.ezcode.codetest.domain.user.model.enums.Noun;
import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.exception.code.AuthExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.repository.UserAuthTypeRepository;
import org.ezcode.codetest.domain.user.repository.UserGithubInfoRepository;
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
	private final UserGithubInfoRepository userGithubInfoRepository;
	private final PasswordEncoder passwordEncoder;
	private static final java.util.Random RANDOM = new java.util.Random();

	public void checkEmailUnique(String email) {
		log.info("Checking email unique: {}", email);
		Optional<User> findUser = userRepository.findByEmail(email);
		log.info("Found user: {}", findUser);
		//이미 EMAIL로 가입한 유저면 에러
		if (findUser.isPresent()
			&& userAuthTypeRepository.getUserAuthType(findUser.get()).contains(AuthType.EMAIL)) {
			log.info("Email and AuthType already in use: {}", email);
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
			.orElseThrow(() -> new AuthException(AuthExceptionCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new AuthException(AuthExceptionCode.PASSWORD_NOT_MATCH);
		}
	}

	public String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	//유저의 AuthType을 리스트형태로 반환
	public List<AuthType> getUserAuthTypes(User user) {
		return userAuthTypeRepository.getUserAuthType(user);
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

	public void decreaseReviewToken(User user) {

		if (user.getReviewToken() <= 0) {
			throw new UserException(UserExceptionCode.NOT_ENOUGH_TOKEN);
		}

		userRepository.decreaseReviewToken(user);
	}

	public void resetReviewTokensForUsers(UsersByWeek users) {
		userRepository.updateReviewTokens(users.fullWeek(), 40);
		userRepository.updateReviewTokens(users.partialWeek(), 20);
	}

	private static String generateRandomNickname() {
		Adjective adjective = Adjective.values()[RANDOM.nextInt(Adjective.values().length)];
		Noun noun = Noun.values()[RANDOM.nextInt(Noun.values().length)];
		int number = RANDOM.nextInt(1000);
		return adjective.name() + noun.name() + number;
	}

	public User getUserByEmail(String email) {
		return userRepository.getUserByEmail(email);
	}

	public void updateUserGithubAccessToken(UserGithubInfo userGithubInfo) {
		userGithubInfoRepository.updateGithubAccessToken(userGithubInfo);
	}

	public void createUserGithubInfo(UserGithubInfo userGithubInfo) {
		userGithubInfoRepository.createUserGithubInfo(userGithubInfo);
	}

	public UserGithubInfo getUserGithubInfoById(Long id) {
		return userGithubInfoRepository.getUserGithubInfo(id);
	}
}
