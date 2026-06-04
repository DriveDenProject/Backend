package com.driveden.app.infrastructure.out.persistence.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface FuelLogEfficiencyProjection {

    Integer getKmAtFill();

    BigDecimal getGallons();

    LocalDateTime getFilledAt();
}
