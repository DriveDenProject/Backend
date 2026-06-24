package com.driveden.app.domain.voice.exception;

public class VoiceClassificationException extends RuntimeException {

    public VoiceClassificationException(String message) {
        super(message);
    }

    public VoiceClassificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
