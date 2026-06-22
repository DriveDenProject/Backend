package com.driveden.app.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.driveden.app.domain.cars.dto.makesDTO;
import com.driveden.app.domain.cars.dto.modelByGenerationDTO;
import com.driveden.app.domain.cars.dto.modelsDTO;

@Component
public class CarSpecsClient {

    private final WebClient webClient;

    public CarSpecsClient(@Qualifier("carSpecsWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public String getCarSpecs(String model) {
        return webClient.get()
                .uri("/models?name=" + model)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public List<makesDTO> getAllMakes() {
        return webClient.get()
                .uri("/cars/makes")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<makesDTO>>() {})
                .block();
    }

    public List<modelsDTO> getModelsByMake(String makeId) {
        return webClient.get()
                .uri("/cars/makes/" + makeId + "/models")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<modelsDTO>>() {})
                .block();
    }

    public List<modelByGenerationDTO> getModelsByGeneration(String modelId) {
        return webClient.get()
                .uri("/cars/models/" + modelId + "/generations")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<modelByGenerationDTO>>() {})
                .block();
    }
}