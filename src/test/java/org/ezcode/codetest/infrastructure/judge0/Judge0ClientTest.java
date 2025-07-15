package org.ezcode.codetest.infrastructure.judge0;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.ezcode.codetest.application.submission.dto.request.compile.CodeCompileRequest;
import org.ezcode.codetest.application.submission.dto.response.compile.ExecutionResultResponse;
import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.ezcode.codetest.domain.submission.exception.code.SubmissionExceptionCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@DisplayName("Judge0Client 단위 테스트")
public class Judge0ClientTest {

    @InjectMocks
    private Judge0Client judge0Client;

    @Mock
    private Judge0ResponseMapper interpreter;

    @Mock
    private CodeCompileRequest request;

    @Mock
    private JudgeResult result;

    private MockWebServer mockWebServer;

    @BeforeEach
    void setup() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/").toString();
        ReflectionTestUtils.setField(judge0Client, "judge0ApiUrl", baseUrl);
        ReflectionTestUtils.invokeMethod(judge0Client, "init");

        WebClient original = (WebClient)ReflectionTestUtils.getField(judge0Client, "webClient");
        WebClient testClient = Objects.requireNonNull(original).mutate()
            .filter((req, next) ->
                next.exchange(req)
                    .flatMap(resp -> resp.statusCode().is5xxServerError()
                        ? Mono.error(new SubmissionException(SubmissionExceptionCode.COMPILE_SERVER_ERROR))
                        : Mono.just(resp))
            )
            .filter((req, next) ->
                next.exchange(req)
                    .onErrorResume(java.util.concurrent.TimeoutException.class,
                        ex -> Mono.error(new SubmissionException(SubmissionExceptionCode.COMPILE_TIMEOUT)))
            )
            .build();

        ReflectionTestUtils.setField(judge0Client, "webClient", testClient);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("성공 -> 서버가 토큰을 반환하면 그대로 리턴")
        void submitAndGetToken_success() {

            // given
            mockWebServer.enqueue(new MockResponse()
                .setBody("{\"token\":\"token\"}")
                .setHeader("Content-Type", "application/json")
            );

            // when
            String token = judge0Client.submitAndGetToken(request);

            // then
            assertThat(token).isEqualTo("token");
        }

        @Test
        @DisplayName("성공 -> status.id >= 3 이면 interpreter 결과 리턴")
        void pollUntilDone_success() {

            // given
            mockWebServer.enqueue(json("{\"status\":{\"id\":1}}"));
            mockWebServer.enqueue(json("{\"status\":{\"id\":3}}"));

            given(interpreter.toJudgeResult(any(ExecutionResultResponse.class))).willReturn(result);

            // when
            JudgeResult judgeResult = judge0Client.pollUntilDone("token");

            // then
            assertThat(judgeResult).isSameAs(result);
            ArgumentCaptor<ExecutionResultResponse> captor =
                ArgumentCaptor.forClass(ExecutionResultResponse.class);
            then(interpreter).should().toJudgeResult(captor.capture());

            ExecutionResultResponse captured = captor.getValue();
            assertThat(captured.status().id()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("서버 에러 -> HTTP 500 응답 시 COMPILE_SERVER_ERROR 예외 발생")
        void submitAndGetToken_throwServerError() {

            // given
            mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
            );

            // when & then
            assertThatThrownBy(() -> judge0Client.submitAndGetToken(request))
                .isInstanceOf(SubmissionException.class)
                .hasMessage(SubmissionExceptionCode.COMPILE_SERVER_ERROR.getMessage());
        }

        @Test
        @DisplayName("타임아웃 -> 응답 지연 시 COMPILE_TIMEOUT 예외 발생")
        void submitAndGetToken_throwTimeout() {

            // given
            mockWebServer.enqueue(new MockResponse()
                .setBody("{\"token\":\"late\"}")
                .setHeader("Content-Type", "application/json")
                .setBodyDelay(11, TimeUnit.SECONDS));

            // when & then
            assertThatThrownBy(() -> judge0Client.submitAndGetToken(request))
                .isInstanceOf(SubmissionException.class)
                .hasMessage(SubmissionExceptionCode.COMPILE_TIMEOUT.getMessage());
        }

        @Test
        @DisplayName("컴파일 에러 매핑 -> BadRequest 시 ofCompileError -> interpreter 호출")
        void pollUntilDone_compileErrorMapping() {

            // given
            mockWebServer.enqueue(new MockResponse().setResponseCode(400));
            mockWebServer.enqueue(json("{\"status\":{\"id\":3}}"));

            ExecutionResultResponse errorResp = ExecutionResultResponse.ofCompileError();
            given(interpreter.toJudgeResult(errorResp)).willReturn(result);

            // when
            JudgeResult judgeResult = judge0Client.pollUntilDone("token");

            // then
            assertThat(judgeResult).isSameAs(result);
            then(interpreter).should().toJudgeResult(errorResp);
        }

        @Test
        @DisplayName("타임아웃 -> 60초 경과 시 COMPILE_TIMEOUT 예외 발생")
        void pollUntilDone_throwTimeout() {

            // given
            for (int i = 0; i < 61; i++) {
                mockWebServer.enqueue(json("{\"status\":{\"id\":1}}"));
            }

            // when & then
            assertThatThrownBy(() -> judge0Client.pollUntilDone("token"))
                .isInstanceOf(SubmissionException.class)
                .hasMessage(SubmissionExceptionCode.COMPILE_TIMEOUT.getMessage());
        }

    }

    private MockResponse json(String body) {
        return new MockResponse()
            .setBody(body)
            .setHeader("Content-Type", "application/json");
    }
}
