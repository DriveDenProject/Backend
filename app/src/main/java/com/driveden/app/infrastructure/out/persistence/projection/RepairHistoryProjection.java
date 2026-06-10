package com.driveden.app.infrastructure.out.persistence.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface RepairHistoryProjection {

    Long getRepairId();

    String getName();

    BigDecimal getCost();

    LocalDateTime getRepairDate();
}
