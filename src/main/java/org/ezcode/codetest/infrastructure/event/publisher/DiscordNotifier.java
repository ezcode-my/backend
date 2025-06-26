package org.ezcode.codetest.infrastructure.event.publisher;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ezcode.codetest.application.submission.port.ExceptionNotifier;
import org.ezcode.codetest.domain.submission.exception.CodeReviewException;
import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DiscordNotifier implements ExceptionNotifier {

    @Value("${discord.webhook.url}")
    private String webhookUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void notifyException(String methodName, Throwable t) {
        String title = "채점 시스템 예외";
        String description;
        String exceptionDetail;

        if (t instanceof SubmissionException se) {
            var code = se.getResponseCode();
            description = "채점 중 SubmissionException 발생";
            exceptionDetail =
                """
                    • 성공 여부: %s
                    • 상태코드: %s
                    • 메시지: %s
                    """.formatted(code.isSuccess(), code.getStatus(), code.getMessage());
        } else if (t instanceof CodeReviewException ce) {
            var code = ce.getResponseCode();
            description = "코드 리뷰 중 CodeReviewException 발생";
            exceptionDetail =
                """
                    • 성공 여부: %s
                    • 상태코드: %s
                    • 메시지: %s
                    """.formatted(code.isSuccess(), code.getStatus(), code.getMessage());
        } else {
            description = "채점 중 알 수 없는 예외 발생";
            exceptionDetail =
                """
                    • 성공 여부: false
                    • 상태코드: 500
                    • 메시지: %s
                    """.formatted(Optional.ofNullable(t.getMessage()).orElse("No message"));
        }

        sendEmbed(title, description, exceptionDetail, methodName);
    }

    private void sendEmbed(String title, String description, String exception, String methodName) {
        try {
            Map<String, Object> embed = Map.of(
                "title", title,
                "description", description,
                "color", 16711680,
                "fields", List.of(
                    Map.of(
                        "name", "예외 메시지",
                        "value", exception,
                        "inline", false
                    ),
                    Map.of(
                        "name", "발생 메서드",
                        "value", methodName,
                        "inline", false
                    ),
                    Map.of(
                        "name", "발생 시각",
                        "value", Instant.now().toString(),
                        "inline", false
                    )
                )
            );

            Map<String, Object> payload = Map.of(
                "embeds", List.of(embed)
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = objectMapper.writeValueAsString(payload);

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            restTemplate.postForEntity(webhookUrl, entity, String.class);
        } catch (Exception e) {
            log.error("Discord 웹훅 전송 실패", e);
        }
    }
}

