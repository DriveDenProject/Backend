package com.driveden.app.infrastructure.out.persistence.projection;

import java.time.LocalDateTime;

public interface LatestRepairByCategoryProjection {

    String getDescription();

    LocalDateTime getRepairDate();
}
