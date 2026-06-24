package com.driveden.app.domain.voice.dto;

import java.util.Map;

import com.driveden.app.domain.voice.model.VoiceClassificationType;
import com.fasterxml.jackson.annotation.JsonInclude;

public record VoiceClassificationResponseDTO(
        VoiceClassificationType type,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Map<String, Object> data,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String message
) {
}
