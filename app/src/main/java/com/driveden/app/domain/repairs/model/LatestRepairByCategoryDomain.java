package com.driveden.app.domain.repairs.model;

import java.time.LocalDate;

public record LatestRepairByCategoryDomain(
        String description,
        LocalDate repairDate
) {
}
