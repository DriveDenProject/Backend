package com.driveden.app.domain.voice.service;

import com.driveden.app.domain.voice.model.VoiceClassificationResult;

public interface VoiceInputClassifier {

    VoiceClassificationResult classify(String text);
}
