package com.driveden.app.application.usecase;

import org.springframework.stereotype.Service;

import com.driveden.app.domain.voice.dto.VoiceClassificationResponseDTO;
import com.driveden.app.domain.voice.exception.VoiceClassificationException;
import com.driveden.app.domain.voice.model.VoiceClassificationResult;
import com.driveden.app.domain.voice.model.VoiceLanguage;
import com.driveden.app.domain.voice.service.VoiceInputClassifier;
import com.driveden.app.infrastructure.ai.OpenAIRateLimiter;
import com.driveden.app.infrastructure.ai.VoiceInputPrefilter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessVoiceInputUseCase {

    private final VoiceInputPrefilter prefilter;
    private final VoiceInputClassifier classifier;
    private final OpenAIRateLimiter rateLimiter;
    private final VoiceInputDuplicateCache duplicateCache;

    public VoiceClassificationResponseDTO process(String text, Long userId, String ipAddress) {
        String normalizedText = text == null ? "" : text.trim();
        VoiceLanguage language = prefilter.language(normalizedText);

        if (prefilter.shouldReject(normalizedText)) {
            rateLimiter.recordFailure(userId);
            return toResponse(invalid(language));
        }

        return duplicateCache.get(userId, normalizedText)
                .map(this::toResponse)
                .orElseGet(() -> classifyAndCache(normalizedText, userId, ipAddress, language));
    }

    private VoiceClassificationResponseDTO classifyAndCache(
            String text,
            Long userId,
            String ipAddress,
            VoiceLanguage language
    ) {
        rateLimiter.check(userId, ipAddress);

        try {
            VoiceClassificationResult result = classifier.classify(text);
            duplicateCache.put(userId, text, result);
            rateLimiter.recordSuccess(userId);
            return toResponse(result);
        } catch (VoiceClassificationException ex) {
            rateLimiter.recordFailure(userId);
            VoiceClassificationResult result = invalid(language);
            duplicateCache.put(userId, text, result);
            return toResponse(result);
        }
    }

    private VoiceClassificationResult invalid(VoiceLanguage language) {
        String message = language == VoiceLanguage.SPANISH
                ? "Audio no corresponde a registros vehiculares"
                : "Audio not related to vehicle records";
        return VoiceClassificationResult.invalid(message, language);
    }

    private VoiceClassificationResponseDTO toResponse(VoiceClassificationResult result) {
        return new VoiceClassificationResponseDTO(result.type(), result.data(), result.message());
    }
}
