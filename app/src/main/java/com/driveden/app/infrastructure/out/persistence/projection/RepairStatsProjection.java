package com.driveden.app.infrastructure.out.persistence.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface RepairStatsProjection {

    LocalDateTime getLastRepair();

    BigDecimal getTotalSpent();

    Long getTotalRepairs();
}
