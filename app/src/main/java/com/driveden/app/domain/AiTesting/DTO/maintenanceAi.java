package com.driveden.app.domain.AiTesting.DTO;

import com.driveden.app.domain.AiTesting.ENUM.Intent;

public record maintenanceAi(
        Intent intent,
        String vehicle,
        Integer year,
        String action
) {

}


