package com.driveden.app.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.motorcycles.dto.ArticleIdResponseDTO;
import com.driveden.app.domain.motorcycles.dto.MotorcycleImageDTO;
import com.driveden.app.domain.motorcycles.dto.MotorcycleMakeDTO;
import com.driveden.app.domain.motorcycles.dto.MotorcycleModelDTO;
import com.driveden.app.domain.motorcycles.dto.MotorcycleModelDetailsDTO;
import com.driveden.app.domain.motorcycles.dto.MotorcycleProductionYearDTO;

import reactor.core.publisher.Mono;

@Component
public class MotorcycleClient {

    private final WebClient webClient;

    public MotorcycleClient(@Qualifier("motorcycleWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public List<MotorcycleMakeDTO> getAllMakes() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/make")
                        .build())
                .retrieve()
                .onStatus(status -> status.value() == 404, this::handleNotFound)
                .onStatus(status -> status.value() == 429, this::handleRateLimit)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleServerError)
                .onStatus(HttpStatusCode::isError, this::handleMotorcycleApiError)
                .bodyToMono(new ParameterizedTypeReference<List<MotorcycleMakeDTO>>() {})
                .block();
    }

    public List<MotorcycleModelDTO> getModelsByMake(Long makeId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/model/make-id/{makeId}")
                        .build(makeId))
                .retrieve()
                .onStatus(status -> status.value() == 404, this::handleNotFound)
                .onStatus(status -> status.value() == 429, this::handleRateLimit)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleServerError)
                .onStatus(HttpStatusCode::isError, this::handleMotorcycleApiError)
                .bodyToMono(new ParameterizedTypeReference<List<MotorcycleModelDTO>>() {})
                .block();
    }

    public MotorcycleModelDetailsDTO getModelDetails(String modelId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/models/{modelId}")
                        .build(modelId))
                .retrieve()
                .onStatus(status -> status.value() == 404, this::handleNotFound)
                .onStatus(status -> status.value() == 429, this::handleRateLimit)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleServerError)
                .onStatus(HttpStatusCode::isError, this::handleMotorcycleApiError)
                .bodyToMono(MotorcycleModelDetailsDTO.class)
                .block();
    }

    public List<MotorcycleProductionYearDTO> getProductionYears(Long modelId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/model/{modelId}/years")
                        .build(modelId))
                .retrieve()
                .onStatus(status -> status.value() == 404, this::handleNotFound)
                .onStatus(status -> status.value() == 429, this::handleRateLimit)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleServerError)
                .onStatus(HttpStatusCode::isError, this::handleMotorcycleApiError)
                .bodyToMono(new ParameterizedTypeReference<List<MotorcycleProductionYearDTO>>() {})
                .block();
    }

    public Long getArticleId(Integer year, String make, String model) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/article/{year}/{make}/{model}")
                        .build(year, make, model))
                .retrieve()
                .onStatus(status -> status.value() == 404, this::handleNotFound)
                .onStatus(status -> status.value() == 429, this::handleRateLimit)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleServerError)
                .onStatus(HttpStatusCode::isError, this::handleMotorcycleApiError)
                .bodyToMono(ArticleIdResponseDTO.class)
                .map(this::extractArticleId)
                .block();
    }

    public String getMotorcycleImageLink(Long articleId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/article/{articleId}/image/link")
                        .build(articleId))
                .retrieve()
                .onStatus(status -> status.value() == 404, this::handleNotFound)
                .onStatus(status -> status.value() == 429, this::handleRateLimit)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleServerError)
                .onStatus(HttpStatusCode::isError, this::handleMotorcycleApiError)
                .bodyToMono(MotorcycleImageDTO.class)
                .map(this::extractImageLink)
                .block();
    }

    private Long extractArticleId(ArticleIdResponseDTO response) {
        if (response == null
                || response.getArticleCompleteInfo() == null
                || response.getArticleCompleteInfo().getArticleId() == null) {
            throw new CustomException(
                    "Motorcycles API response does not contain articleCompleteInfo.articleID",
                    HttpStatus.BAD_GATEWAY,
                    "MOTORCYCLES_API_INVALID_RESPONSE");
        }

        return response.getArticleCompleteInfo().getArticleId();
    }

    private String extractImageLink(MotorcycleImageDTO response) {
        if (response == null || response.getLink() == null || response.getLink().isBlank()) {
            throw new CustomException(
                    "Motorcycles API response does not contain image link",
                    HttpStatus.BAD_GATEWAY,
                    "MOTORCYCLES_API_INVALID_RESPONSE");
        }

        return response.getLink();
    }

    private Mono<Throwable> handleNotFound(ClientResponse response) {
        return buildExternalApiException(response, HttpStatus.NOT_FOUND, "MOTORCYCLES_API_NOT_FOUND");
    }

    private Mono<Throwable> handleRateLimit(ClientResponse response) {
        return buildExternalApiException(response, HttpStatus.TOO_MANY_REQUESTS, "MOTORCYCLES_API_RATE_LIMIT");
    }

    private Mono<Throwable> handleServerError(ClientResponse response) {
        return buildExternalApiException(response, HttpStatus.BAD_GATEWAY, "MOTORCYCLES_API_SERVER_ERROR");
    }

    private Mono<Throwable> handleMotorcycleApiError(ClientResponse response) {
        return buildExternalApiException(response, HttpStatus.BAD_GATEWAY, "MOTORCYCLES_API_ERROR");
    }

    private Mono<Throwable> buildExternalApiException(
            ClientResponse response,
            HttpStatus status,
            String code
    ) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty("No response body")
                .map(body -> new CustomException(
                        "Motorcycles API request failed with status "
                                + response.statusCode().value()
                                + ": "
                                + body,
                        status,
                        code));
    }
}