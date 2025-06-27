package org.ezcode.codetest.domain.user.service;

import java.util.Map;

import org.ezcode.codetest.application.usermanagement.user.dto.response.GithubOAuth2Response;
import org.ezcode.codetest.application.usermanagement.user.dto.response.GoogleOAuth2Response;
import org.ezcode.codetest.application.usermanagement.user.dto.response.OAuth2Response;
import org.ezcode.codetest.domain.user.model.entity.CustomOAuth2User;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.entity.UserAuthType;
import org.ezcode.codetest.domain.user.model.entity.UserFactory;
import org.ezcode.codetest.domain.user.model.entity.UserGithubInfo;
import org.ezcode.codetest.domain.user.model.enums.AuthType;
import org.ezcode.codetest.domain.user.repository.UserAuthTypeRepository;
import org.ezcode.codetest.domain.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;
	private final UserAuthTypeRepository userAuthTypeRepository;
	private final UserDomainService userDomainService;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		//부모 클래스에 존재하는 loadUser 메서드에 userRequest인자를 넣어 유저 정보를 가져온다
		OAuth2User oAuth2User = super.loadUser(userRequest);

		log.info("loadUser : {}", oAuth2User);

		//가져온 인증 정보가 구글인지, 깃헙인지 알아내기 위한 변수
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		OAuth2Response oAuth2Response = createResponse(registrationId, oAuth2User.getAttributes());

		String resolvedEmail = oAuth2Response.getEmail();

		User findUser = userDomainService.getUserByEmail(resolvedEmail);
		AuthType authType = AuthType.from(oAuth2Response.getProvider().toUpperCase());

		processUser(findUser, oAuth2Response, authType, registrationId);
		return new CustomOAuth2User(oAuth2Response);

	}

	private OAuth2Response createResponse(String provider, Map<String, Object> attributes) {
		return switch (provider.toLowerCase()) {
			case "google" -> new GoogleOAuth2Response(attributes);
			case "github" -> new GithubOAuth2Response(attributes);
			default -> throw new OAuth2AuthenticationException("Unsupported provider");
		};
	}

	//신규인지 존재하는 유저인지
	private void processUser(
		User user,
		OAuth2Response response,
		AuthType authType,
		String provider
	) {
		if (user == null) {
			createNewUser(response, authType, provider);
		} else {
			updateExistingUser(user, response, authType, provider);
		}
	}

	private void createNewUser(OAuth2Response response, AuthType authType, String provider) {
		String nickname = userDomainService.generateUniqueNickname();
		User newUser = UserFactory.createSocialUser(response, nickname, provider);
		newUser.setVerified();
		newUser.setReviewToken(20);
		userRepository.createUser(newUser);
		userAuthTypeRepository.createUserAuthType(new UserAuthType(newUser, authType));
		updateGithubUrl(newUser, response, provider);
	}

	private void updateExistingUser(User user, OAuth2Response response, AuthType authType, String provider) {
		if (!userDomainService.getUserAuthTypes(user).contains(authType)) {
			if (!user.isVerified()) {
				user.setVerified();
				user.setReviewToken(20);
			}
			userAuthTypeRepository.createUserAuthType(new UserAuthType(user, authType));
			updateGithubUrl(user, response, provider);
		}
	}
	private void updateGithubUrl(User user, OAuth2Response response, String provider) {
		if ("github".equals(provider)) {
			user.setGithubUrl(response.getGithubUrl());

			UserGithubInfo userGithubInfo =
				UserGithubInfo.builder()
					.owner(response.getOwner())
					.user(user)
					.build();

			userDomainService.createUserGithubInfo(userGithubInfo);
		}
	}

}
