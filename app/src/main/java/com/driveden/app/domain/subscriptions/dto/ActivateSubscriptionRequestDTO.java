package com.driveden.app.domain.subscriptions.dto;

import jakarta.validation.constraints.NotBlank;

public record ActivateSubscriptionRequestDTO(
        @NotBlank String provider,
        @NotBlank String purchaseToken
) {
}
