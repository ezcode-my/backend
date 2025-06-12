package org.ezcode.codetest.application.usermanagement.user.dto.response;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Google OAuth2 응답 처리 클래스")
public class GoogleOAuth2Response implements OAuth2Response {
	private final Map<String, Object> attributes;

	//구글의 경우 정보들이 카테고리로 묶여있는게 아니라 흩뿌려져있기 때문에 바로 생성자로 받기
	public GoogleOAuth2Response(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	@Schema(description = "OAuth 제공자 이름", example = "google")
	public String getProvider() {
		return "google";
	}

	@Override
	@Schema(description = "제공자 내부 식별자", example = "109000123456789012345")
	public String getProviderId() {
		//구글에서는 "sub"으로 제공함
		return attributes.get("sub").toString();
	}

	@Override
	@Schema(description = "사용자 이메일", example = "user@gmail.com")
	public String getEmail() {
		return attributes.get("email").toString();
	}

	@Override
	@Schema(description = "사용자 이름", example = "홍길동")
	public String getName() {
		return attributes.get("name").toString();
	}

}
