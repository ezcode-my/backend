package org.ezcode.codetest.domain.problem.model.dto;

import lombok.Builder;

@Builder
public record AnswerEvaluation(

	boolean isCorrect,

	String expectedOutput,

	String actualOutput

) {
}
