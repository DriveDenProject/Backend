package com.driveden.app.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.driveden.app.application.services.SubscriptionService;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.voice.dto.VoiceClassificationResponseDTO;
import com.driveden.app.domain.voice.exception.VoiceClassificationException;
import com.driveden.app.domain.voice.model.VoiceClassificationResult;
import com.driveden.app.domain.voice.model.VoiceClassificationType;
import com.driveden.app.domain.voice.model.VoiceLanguage;
import com.driveden.app.domain.voice.service.VoiceInputClassifier;
import com.driveden.app.infrastructure.ai.OpenAIRateLimiter;
import com.driveden.app.infrastructure.ai.VoiceInputPrefilter;

class ProcessVoiceInputUseCaseTest {

    private final Clock clock = Clock.fixed(Instant.parse("2026-06-24T12:00:00Z"), ZoneOffset.UTC);

    @Test
    void rejectsInvalidUnrelatedAudioBeforeClassifierCall() {
        AtomicInteger calls = new AtomicInteger();
        ProcessVoiceInputUseCase useCase = useCase(text -> {
            calls.incrementAndGet();
            return fuelResult();
        });

        VoiceClassificationResponseDTO response = useCase.process("hello how are you", 1L, "127.0.0.1");

        assertThat(response.type()).isEqualTo(VoiceClassificationType.INVALID_AUDIO);
        assertThat(response.message()).isEqualTo("Audio not related to vehicle records");
        assertThat(calls).hasValue(0);
    }

    @Test
    void preservesSpanishInvalidMessage() {
        ProcessVoiceInputUseCase useCase = useCase(text -> fuelResult());

        VoiceClassificationResponseDTO response = useCase.process("hola como estas", 1L, "127.0.0.1");

        assertThat(response.type()).isEqualTo(VoiceClassificationType.INVALID_AUDIO);
        assertThat(response.message()).isEqualTo("Audio no corresponde a registros vehiculares");
    }

    @Test
    void returnsEnglishClassificationData() {
        ProcessVoiceInputUseCase useCase = useCase(text -> new VoiceClassificationResult(
                VoiceClassificationType.REMINDER,
                Map.of("description", "Oil change every 6 months"),
                null,
                VoiceLanguage.ENGLISH
        ));

        VoiceClassificationResponseDTO response = useCase.process("oil change every 6 months", 1L, "127.0.0.1");

        assertThat(response.type()).isEqualTo(VoiceClassificationType.REMINDER);
        assertThat(response.data()).containsEntry("description", "Oil change every 6 months");
    }

    @Test
    void avoidsRepeatedOpenAiCallsForDuplicateRequest() {
        AtomicInteger calls = new AtomicInteger();
        ProcessVoiceInputUseCase useCase = useCase(text -> {
            calls.incrementAndGet();
            return fuelResult();
        });

        useCase.process("I filled gas 10 gallons today", 1L, "127.0.0.1");
        VoiceClassificationResponseDTO duplicate = useCase.process("I filled gas 10 gallons today", 1L, "127.0.0.1");

        assertThat(duplicate.type()).isEqualTo(VoiceClassificationType.FUEL_LOG);
        assertThat(calls).hasValue(1);
    }

    @Test
    void throwsWhenRateLimitExceeded() {
        ProcessVoiceInputUseCase useCase = useCase(text -> fuelResult());

        for (int i = 0; i < 20; i++) {
            useCase.process("I filled gas 10 gallons today " + i, 1L, "127.0.0.1");
        }

        assertThatThrownBy(() -> useCase.process("I filled gas 10 gallons today limit", 1L, "127.0.0.1"))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("rate limit");
    }

    @Test
    void malformedAiResponseFailsSafely() {
        ProcessVoiceInputUseCase useCase = useCase(text -> {
            throw new VoiceClassificationException("AI response is not valid JSON");
        });

        VoiceClassificationResponseDTO response = useCase.process("I filled gas 10 gallons today", 1L, "127.0.0.1");

        assertThat(response.type()).isEqualTo(VoiceClassificationType.INVALID_AUDIO);
        assertThat(response.message()).isEqualTo("Audio not related to vehicle records");
    }

    @Test
    void consumesAudioUsageAfterSuccessfulClassification() {
        SubscriptionService subscriptionService = mock(SubscriptionService.class);
        ProcessVoiceInputUseCase useCase = useCase(text -> fuelResult(), subscriptionService);

        useCase.process("I filled gas 10 gallons today", 1L, "127.0.0.1");

        verify(subscriptionService).enforceCanUseAudio(1L);
        verify(subscriptionService).consumeAudioUsage(1L);
    }

    private ProcessVoiceInputUseCase useCase(VoiceInputClassifier classifier) {
        return useCase(classifier, null);
    }

    private ProcessVoiceInputUseCase useCase(
            VoiceInputClassifier classifier,
            SubscriptionService subscriptionService
    ) {
        return new ProcessVoiceInputUseCase(
                new VoiceInputPrefilter(),
                classifier,
                new OpenAIRateLimiter(clock),
                new VoiceInputDuplicateCache(clock),
                new VoiceRepairPostProcessor(new RepairCostExtractor()),
                subscriptionService
        );
    }

    private VoiceClassificationResult fuelResult() {
        return new VoiceClassificationResult(
                VoiceClassificationType.FUEL_LOG,
                Map.of("gallons", "10.5", "gasStation", "Terpel"),
                null,
                VoiceLanguage.ENGLISH
        );
    }
}
