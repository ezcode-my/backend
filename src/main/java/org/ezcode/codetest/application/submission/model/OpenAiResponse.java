package org.ezcode.codetest.application.submission.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAiResponse(
	List<Choice> choices
) {
	public record Choice(Message message) {}
	public record Message(String role, String content) {}

	public String getReviewContent() {
		if (choices == null || choices.isEmpty()) return "";
		Message message = choices.get(0).message;
		if (message == null || message.content == null) return "";
		return message.content;
	}
}
