package com.driveden.app.application.usecase;

import org.springframework.stereotype.Service;

import com.driveden.app.application.services.SubscriptionService;
import com.driveden.app.domain.voice.dto.VoiceClassificationResponseDTO;
import com.driveden.app.domain.voice.exception.VoiceClassificationException;
import com.driveden.app.domain.voice.model.VoiceClassificationResult;
import com.driveden.app.domain.voice.model.VoiceLanguage;
import com.driveden.app.domain.voice.model.VoiceClassificationType;
import com.driveden.app.domain.voice.service.VoiceInputClassifier;
import com.driveden.app.infrastructure.ai.OpenAIRateLimiter;
import com.driveden.app.infrastructure.ai.VoiceInputPrefilter;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProcessVoiceInputUseCase {

    private final VoiceInputPrefilter prefilter;
    private final VoiceInputClassifier classifier;
    private final OpenAIRateLimiter rateLimiter;
    private final VoiceInputDuplicateCache duplicateCache;
    private final VoiceRepairPostProcessor repairPostProcessor;
    private final SubscriptionService subscriptionService;

    @Autowired
    public ProcessVoiceInputUseCase(
            VoiceInputPrefilter prefilter,
            VoiceInputClassifier classifier,
            OpenAIRateLimiter rateLimiter,
            VoiceInputDuplicateCache duplicateCache,
            VoiceRepairPostProcessor repairPostProcessor,
            SubscriptionService subscriptionService
    ) {
        this.prefilter = prefilter;
        this.classifier = classifier;
        this.rateLimiter = rateLimiter;
        this.duplicateCache = duplicateCache;
        this.repairPostProcessor = repairPostProcessor;
        this.subscriptionService = subscriptionService;
    }

    public ProcessVoiceInputUseCase(
            VoiceInputPrefilter prefilter,
            VoiceInputClassifier classifier,
            OpenAIRateLimiter rateLimiter,
            VoiceInputDuplicateCache duplicateCache,
            VoiceRepairPostProcessor repairPostProcessor
    ) {
        this(prefilter, classifier, rateLimiter, duplicateCache, repairPostProcessor, null);
    }

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
        if (subscriptionService != null) {
            subscriptionService.enforceCanUseAudio(userId);
        }

        rateLimiter.check(userId, ipAddress);

        try {
            VoiceClassificationResult result = repairPostProcessor.process(text, classifier.classify(text));
            duplicateCache.put(userId, text, result);
            rateLimiter.recordSuccess(userId);
            if (subscriptionService != null && result.type() != VoiceClassificationType.INVALID_AUDIO) {
                subscriptionService.consumeAudioUsage(userId);
            }
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
