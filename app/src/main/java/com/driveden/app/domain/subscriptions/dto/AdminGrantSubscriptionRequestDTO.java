package com.driveden.app.domain.subscriptions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminGrantSubscriptionRequestDTO(
        @NotNull Long userId,
        @NotBlank String planCode
) {
}
