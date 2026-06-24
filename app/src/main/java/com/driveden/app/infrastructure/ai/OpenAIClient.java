package com.driveden.app.infrastructure.ai;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.driveden.app.domain.voice.exception.VoiceClassificationException;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class OpenAIClient {

    private static final String MODEL = "gpt-4.1-mini";
    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    private final WebClient webClient;

    public OpenAIClient(@Qualifier("openAIWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public String classify(String prompt) {
        Map<String, Object> request = Map.of(
                "model", MODEL,
                "temperature", 0,
                "response_format", Map.of("type", "json_object"),
                "messages", List.of(
                        Map.of("role", "system", "content", "You extract vehicle record JSON. JSON only."),
                        Map.of("role", "user", "content", prompt)
                )
        );

        try {
            JsonNode response = webClient.post()
                    .uri("/chat/completions")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block(TIMEOUT);

            JsonNode content = response == null
                    ? null
                    : response.path("choices").path(0).path("message").path("content");

            if (content == null || content.isMissingNode() || content.asText().isBlank()) {
                throw new VoiceClassificationException("OpenAI response did not contain content");
            }

            return content.asText();
        } catch (VoiceClassificationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new VoiceClassificationException("OpenAI classification failed", ex);
        }
    }
}
