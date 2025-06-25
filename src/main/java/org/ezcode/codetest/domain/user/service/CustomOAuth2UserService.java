package org.ezcode.codetest.domain.user.service;

import java.util.List;
import java.util.UUID;

import org.ezcode.codetest.application.usermanagement.user.dto.response.GithubOAuth2Response;
import org.ezcode.codetest.application.usermanagement.user.dto.response.GoogleOAuth2Response;
import org.ezcode.codetest.application.usermanagement.user.dto.response.OAuth2Response;
import org.ezcode.codetest.domain.user.model.entity.CustomOAuth2User;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.entity.UserAuthType;
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
		log.info("loadUser: {}", oAuth2User);

		//가져온 인증 정보가 구글인지, 깃헙인지 알아내기 위한 변수
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		//google, github에 따라 다르게 반환
		OAuth2Response oAuth2Response = switch (registrationId.toLowerCase()) {
			case "google" -> new GoogleOAuth2Response(oAuth2User.getAttributes());
			case "github" -> new GithubOAuth2Response(oAuth2User.getAttributes());
			default -> throw new OAuth2AuthenticationException("Unsupported provider");
		};

		String email = oAuth2Response.getEmail();
		User findUser = userRepository.getUserByEmail(email);
		AuthType authType = AuthType.from(oAuth2Response.getProvider().toUpperCase());

		if (findUser == null) {
			String nickname = userDomainService.generateUniqueNickname();
			User newUser = User.socialUser(email, oAuth2Response.getName(), nickname, UUID.randomUUID().toString());
			userRepository.createUser(newUser);
			userAuthTypeRepository.createUserAuthType(new UserAuthType(newUser, authType));
		} else {
			if (!userDomainService.getUserAuthTypes(findUser).contains(authType)) {
				userAuthTypeRepository.createUserAuthType(new UserAuthType(findUser, authType));
			}
		}

		return new CustomOAuth2User(oAuth2Response);
	}

}
