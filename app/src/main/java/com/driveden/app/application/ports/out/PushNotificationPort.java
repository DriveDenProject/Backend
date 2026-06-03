package com.driveden.app.application.ports.out;

import java.util.Map;

import com.driveden.app.domain.deviceTokens.model.PushNotificationResult;

public interface PushNotificationPort {

    PushNotificationResult send(String token, String title, String body, Map<String, String> data);
}
