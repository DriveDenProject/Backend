package com.driveden.app.infrastructure.ai;

import org.springframework.stereotype.Component;

import com.driveden.app.domain.voice.model.VoiceClassificationResult;
import com.driveden.app.domain.voice.model.VoiceLanguage;
import com.driveden.app.domain.voice.service.VoiceInputClassifier;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OpenAIVoiceInputClassifier implements VoiceInputClassifier {

    private final OpenAIPromptBuilder promptBuilder;
    private final OpenAIClient openAIClient;
    private final OpenAIResponseParser responseParser;

    @Override
    public VoiceClassificationResult classify(String text) {
        VoiceLanguage language = VoiceInputPrefilter.detectLanguage(text);
        String prompt = promptBuilder.build(text);
        String response = openAIClient.classify(prompt);
        return responseParser.parse(response, language);
    }
}
