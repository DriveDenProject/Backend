package com.driveden.app.domain.voice.model;

import java.util.Map;

public record VoiceClassificationResult(
        VoiceClassificationType type,
        Map<String, Object> data,
        String message,
        VoiceLanguage language
) {

    public static VoiceClassificationResult invalid(String message, VoiceLanguage language) {
        return new VoiceClassificationResult(VoiceClassificationType.INVALID_AUDIO, null, message, language);
    }
}
