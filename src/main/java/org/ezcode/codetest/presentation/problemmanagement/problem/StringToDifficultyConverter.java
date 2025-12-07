package org.ezcode.codetest.presentation.problemmanagement.problem;

import org.ezcode.codetest.domain.problem.exception.ProblemException;
import org.ezcode.codetest.domain.problem.exception.code.ProblemExceptionCode;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
import org.springframework.core.convert.converter.Converter;

public class StringToDifficultyConverter implements Converter<String, Difficulty> {

	@Override
	public Difficulty convert(String source) {
		try {
			return Difficulty.valueOf(source.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new ProblemException(ProblemExceptionCode.DIFFICULTY_NOT_FOUND);
		}
	}
}
