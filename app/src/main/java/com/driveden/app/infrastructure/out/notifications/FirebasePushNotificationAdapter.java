package com.driveden.app.infrastructure.out.notifications;

import java.util.Map;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import com.driveden.app.application.ports.out.PushNotificationPort;
import com.driveden.app.domain.deviceTokens.model.PushNotificationResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class FirebasePushNotificationAdapter implements PushNotificationPort {

    private final ObjectProvider<FirebaseMessaging> firebaseMessagingProvider;

    @Override
    public PushNotificationResult send(String token, String title, String body, Map<String, String> data) {
        FirebaseMessaging firebaseMessaging = firebaseMessagingProvider.getIfAvailable();
        if (firebaseMessaging == null) {
            log.warn("Firebase is not configured. Push notification was not sent.");
            return PushNotificationResult.failure(false, "Firebase is not configured");
        }

        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putAllData(data)
                    .build();

            String providerMessageId = firebaseMessaging.send(message);
            return PushNotificationResult.success(providerMessageId);
        } catch (FirebaseMessagingException e) {
            boolean invalidToken = isInvalidToken(e);
            log.warn("Firebase push notification failed invalidToken={} errorCode={} message={}",
                    invalidToken,
                    e.getMessagingErrorCode(),
                    e.getMessage());
            return PushNotificationResult.failure(invalidToken, e.getMessage());
        }
    }

    private boolean isInvalidToken(FirebaseMessagingException exception) {
        return MessagingErrorCode.UNREGISTERED.equals(exception.getMessagingErrorCode())
                || MessagingErrorCode.INVALID_ARGUMENT.equals(exception.getMessagingErrorCode());
    }
}
