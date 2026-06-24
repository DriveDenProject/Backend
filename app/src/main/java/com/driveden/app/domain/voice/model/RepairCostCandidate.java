package com.driveden.app.domain.voice.model;

import java.math.BigDecimal;

public record RepairCostCandidate(
        String phrase,
        BigDecimal amount
) {
}
