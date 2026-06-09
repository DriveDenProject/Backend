package com.driveden.app.domain.repairs.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RepairResponseDTO(
        Long repairId,
        Long vehicleId,
        LocalDateTime repairDate,
        String description,
        String workshop,
        BigDecimal laborCost,
        BigDecimal totalCost,
        Integer totalPartsRegistered,
        List<RepairPartResponseDTO> parts
) {
}
