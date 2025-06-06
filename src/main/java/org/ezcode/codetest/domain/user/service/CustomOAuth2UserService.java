package org.ezcode.codetest.domain.user.service;

import org.ezcode.codetest.application.usermanagement.user.dto.GoogleOAuth2Response;
import org.ezcode.codetest.application.usermanagement.user.dto.OAuth2Response;
import org.ezcode.codetest.domain.user.model.entity.CustomOAuth2User;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		//부모 클래스에 존재하는 loadUser 메서드에 userRequest인자를 넣어 유저 정보를 가져온다
		OAuth2User oAuth2User = super.loadUser(userRequest);
		log.info("loadUser: {}", oAuth2User);

		//가져온 인증 정보가 구글인지, 깃헙인지 알아내기 위한 변수
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		OAuth2Response oAuth2Response = null;

		//인증 정보를 제공하는 플랫폼마다 형식이 다르기 때문에 이에 맞게 가져와야한다.
		if (registrationId.equals("google")) {
			//인증 정보가 구글일 때
			//구글 dto구현체에서 정보 가져오기
			oAuth2Response = new GoogleOAuth2Response(oAuth2User.getAttributes());
		} else {
			return null;
		}
		// if (registrationId.equals("github")) {
		// 	//인증 정보가 github 일 때
		// }

		return new CustomOAuth2User(oAuth2Response, UserRole.USER); //기본적으로 역할은 USER로 설정
	}
}
