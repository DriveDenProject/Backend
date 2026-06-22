package com.driveden.app.infrastructure.out.persistence.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface FuelLogTankHistoryProjection {

    Long getId();

    LocalDateTime getFilledAt();

    String getNotes();

    String getGasStation();

    BigDecimal getPriceTotal();
}
