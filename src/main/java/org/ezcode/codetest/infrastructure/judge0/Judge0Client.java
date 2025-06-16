package org.ezcode.codetest.infrastructure.judge0;

import org.ezcode.codetest.application.submission.dto.request.compile.CodeCompileRequest;
import org.ezcode.codetest.application.submission.dto.response.compile.ExecutionResultResponse;
import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.ezcode.codetest.application.submission.port.JudgeClient;
import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.ezcode.codetest.domain.submission.exception.code.SubmissionExceptionCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	@Override
	public String submitAndGetToken(CodeCompileRequest request) {
		try {
			ExecutionResultResponse executionResultResponse = webClient.post()
				.uri("/submissions?base64_encoded=false&wait=false")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.retrieve()
				.bodyToMono(ExecutionResultResponse.class)
				.block();

			if (executionResultResponse == null) {
				throw new SubmissionException(SubmissionExceptionCode.COMPILE_SERVER_ERROR);
			}

			return executionResultResponse.token();
		} catch (Exception e) {
			throw new SubmissionException(SubmissionExceptionCode.COMPILE_SERVER_ERROR);
		}
	}

	@Override
	public JudgeResult pollUntilDone(String token) {
		try {
			int maxAttempts = 30;
			int attempt = 0;

			while (attempt < maxAttempts) {
				ExecutionResultResponse executionResultResponse = webClient.get()
					.uri("/submissions/{token}", token)
					.accept(MediaType.APPLICATION_JSON)
					.retrieve()
					.bodyToMono(ExecutionResultResponse.class)
					.block();

				if (executionResultResponse == null) {
					throw new SubmissionException(SubmissionExceptionCode.COMPILE_SERVER_ERROR);
				}

				if (executionResultResponse.status().id() >= 3) {
					return interpreter.toJudgeResult(executionResultResponse);
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new SubmissionException(SubmissionExceptionCode.COMPILE_TIMEOUT);
				}
				attempt++;
			}
			throw new SubmissionException(SubmissionExceptionCode.COMPILE_TIMEOUT);
		} catch (Exception e) {
			throw new SubmissionException(SubmissionExceptionCode.COMPILE_SERVER_ERROR);
		}
	}
}
