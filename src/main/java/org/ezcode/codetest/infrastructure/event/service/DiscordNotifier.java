package org.ezcode.codetest.infrastructure.event.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.ezcode.codetest.application.submission.port.ExceptionNotifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DiscordNotifier implements ExceptionNotifier {

	@Value("${discord.webhook.url}")
	private String webhookUrl;
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void sendEmbed(String title, String description, String exception, String methodName) {
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
			e.printStackTrace();
		}
	}
}

