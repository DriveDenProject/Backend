package com.driveden.app.domain.subscriptions.dto;

import java.time.LocalDateTime;

public record CurrentSubscriptionResponseDTO(
        String plan,
        String planName,
        String status,
        LocalDateTime expiresAt
) {
}
