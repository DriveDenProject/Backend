package com.driveden.app.domain.repairs.dto;

import java.time.LocalDate;

public record LatestRepairByCategoryResponseDTO(
        String description,
        LocalDate repairDate
) {
}
