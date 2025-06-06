package org.ezcode.codetest.infrastructure.judge0;

import org.ezcode.codetest.application.submission.dto.request.compile.CodeCompileRequest;
import org.ezcode.codetest.application.submission.dto.response.compile.ExecutionResultResponse;
import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.ezcode.codetest.application.submission.port.JudgeClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Judge0Client implements JudgeClient {

	@Value("${external.judge0.url}")
	private String judge0ApiUrl;
	private WebClient webClient;
	private final Judge0ResponseMapper interpreter;

	@PostConstruct
	private void init() {
		this.webClient = WebClient.create(judge0ApiUrl);
	}

	public JudgeResult execute(CodeCompileRequest request) {
		ExecutionResultResponse executionResultResponse = webClient.post()
			.uri("/submissions?base64_encoded=false&wait=true")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(request)
			.retrieve()
			.bodyToMono(ExecutionResultResponse.class)
			.block();

		return interpreter.toJudgeResult(executionResultResponse);
	}
}
