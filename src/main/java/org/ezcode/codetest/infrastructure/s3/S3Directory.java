package org.ezcode.codetest.infrastructure.s3;

/**
 * S3 prefix를 enum으로 관리
 * */
public enum S3Directory {
	PROBLEM("problem"),
	PROFILE("profile");

	// 소문자로 패키지 관리 하기 위해 dir추가
	private final String dir;

	S3Directory(String dir) {
		this.dir = dir;
	}

	public String getDir() {
		return dir;
	}
}
