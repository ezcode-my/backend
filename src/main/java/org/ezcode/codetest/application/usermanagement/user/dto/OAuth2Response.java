package org.ezcode.codetest.application.usermanagement.user.dto;

public interface OAuth2Response {
	//제공자가 누군지
	String getProvider();

	//제공자 ID
	String getProviderId();

	//사용자 이메일
	String getEmail();

	//사용자 이름
	String getName();
}
