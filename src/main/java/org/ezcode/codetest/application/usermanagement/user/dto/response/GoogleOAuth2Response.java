package org.ezcode.codetest.application.usermanagement.user.dto.response;

import java.util.Map;

public class GoogleOAuth2Response implements OAuth2Response {
	private final Map<String, Object> attributes;

	//구글의 경우 정보들이 카테고리로 묶여있는게 아니라 흩뿌려져있기 때문에 바로 생성자로 받기
	public GoogleOAuth2Response(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProvider() {
		return "google";
	}

	@Override
	public String getProviderId() {
		//구글에서는 "sub"으로 제공함
		return attributes.get("sub").toString();
	}

	@Override
	public String getEmail() {
		return attributes.get("email").toString();
	}

	@Override
	public String getName() {
		return attributes.get("name").toString();
	}

}
