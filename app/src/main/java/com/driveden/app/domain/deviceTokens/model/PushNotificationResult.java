package com.driveden.app.domain.deviceTokens.model;

public record PushNotificationResult(
        Boolean successful,
        Boolean invalidToken,
        String providerMessageId,
        String errorMessage
) {

    public static PushNotificationResult success(String providerMessageId) {
        return new PushNotificationResult(true, false, providerMessageId, null);
    }

    public static PushNotificationResult failure(Boolean invalidToken, String errorMessage) {
        return new PushNotificationResult(false, invalidToken, null, errorMessage);
    }
}
