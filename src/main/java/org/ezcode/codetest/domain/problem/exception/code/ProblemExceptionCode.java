package org.ezcode.codetest.domain.problem.exception.code;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProblemExceptionCode implements ResponseCode {

	PROBLEM_NOT_FOUND(false, HttpStatus.NOT_FOUND, "삭제 되었거나, 문제를 찾을수 없습니다."),
	DIFFICULTY_NOT_FOUND(false, HttpStatus.NOT_FOUND, "삭제 되었거나, 난이도를 찾을수 없습니다."),
	DUPLICATE_PROBLEM(false, HttpStatus.CONFLICT, "이미 존재하는 문제입니다.");

	private final boolean success;

	private final HttpStatus status;

	private final String message;

}
