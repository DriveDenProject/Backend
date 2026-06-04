package com.driveden.app.domain.odometerLogs.dto;

public record MonthlyMileageStatsResponseDTO(
        String month,
        Integer kmTraveled
) {
}
