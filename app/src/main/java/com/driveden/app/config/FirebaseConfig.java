package com.driveden.app.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FirebaseConfig {

    private final ResourceLoader resourceLoader;

    @Bean
    @ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = true)
    public FirebaseApp firebaseApp(
            @Value("${firebase.config-path:}") String firebaseConfigPath,
            @Value("${firebase.service-account-json:}") String firebaseServiceAccountJson
    ) throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        try (InputStream serviceAccount = resolveServiceAccount(firebaseConfigPath, firebaseServiceAccountJson)) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            return FirebaseApp.initializeApp(options);
        }
    }

    @Bean
    @ConditionalOnBean(FirebaseApp.class)
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    private InputStream resolveServiceAccount(String firebaseConfigPath, String firebaseServiceAccountJson)
            throws IOException {
        Resource serviceAccountResource = resolveServiceAccountResource(firebaseConfigPath);
        if (serviceAccountResource != null && serviceAccountResource.exists()) {
            log.info("Firebase initialized using local service account file");
            return serviceAccountResource.getInputStream();
        }

        if (firebaseServiceAccountJson != null && !firebaseServiceAccountJson.isBlank()) {
            log.info("Firebase initialized using environment variable credentials");
            return new ByteArrayInputStream(firebaseServiceAccountJson.getBytes(StandardCharsets.UTF_8));
        }

        log.error("Firebase credentials not found");
        throw new IllegalStateException("Firebase credentials not found");
    }

    private Resource resolveServiceAccountResource(String firebaseConfigPath) {
        if (firebaseConfigPath == null || firebaseConfigPath.isBlank()) {
            return null;
        }

        if (firebaseConfigPath.startsWith("classpath:")) {
            return resourceLoader.getResource(firebaseConfigPath);
        }

        Path path = Path.of(firebaseConfigPath);
        if (Files.exists(path)) {
            return new FileSystemResource(path);
        }

        return resourceLoader.getResource("classpath:" + firebaseConfigPath);
    }
}
