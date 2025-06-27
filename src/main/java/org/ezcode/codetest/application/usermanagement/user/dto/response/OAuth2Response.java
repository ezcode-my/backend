package org.ezcode.codetest.application.usermanagement.user.dto.response;

public interface OAuth2Response {
	//제공자가 누군지
	String getProvider();

	//제공자 ID
	String getProviderId();

	//사용자 이메일
	String getEmail();

	//사용자 이름
	String getName();

	//깃헙 아이디
	String getGithubId();

	//깃헙 링크
	String getGithubUrl();


	//깃헙 owner
	String getOwner();
}
