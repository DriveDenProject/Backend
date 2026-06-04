package com.driveden.app.infrastructure.out.persistence.projection;

import java.math.BigDecimal;

public interface FuelLogTankHistoryProjection {

    String getNotes();

    String getGasStation();

    BigDecimal getPriceTotal();
}
