package com.driveden.app.domain.subscriptions.dto;

public record ActivateSubscriptionResponseDTO(
        String provider,
        String status,
        String message
) {
}
