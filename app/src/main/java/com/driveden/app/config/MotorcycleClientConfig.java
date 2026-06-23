package com.driveden.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MotorcycleClientConfig {

    @Value("${motorcycles.base-url}")
    private String baseUrl;

    @Value("${rapidapi.motorcycle.key}")
    private String apiKey;

    @Value("${rapidapi.motorcycle.host}")
    private String host;

    @Bean("motorcycleWebClient")
    public WebClient motorcycleWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeader("x-rapidapi-key", apiKey)
                .defaultHeader("x-rapidapi-host", host)
                .build();
    }
}
