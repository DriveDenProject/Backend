package com.driveden.app.domain.repairs.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RepairPartResponseDTO(
        Long partId,
        String name,
        Long categoryId,
        Integer quantity,
        BigDecimal unitPrice,
        LocalDate warrantyExpiration,
        LocalDate partExpiration
) {
}
