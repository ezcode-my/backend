package org.ezcode.codetest.application.submission.model;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAIResponse(
	List<Choice> choices
) {
	public record Choice(Message message) {}
	public record Message(String role, String content) {}

	public String getReviewContent() {
		return Optional.ofNullable(choices)
			.flatMap(list -> list.stream().findFirst())
			.map(choice -> choice.message().content())
			.orElse("");
	}
}
