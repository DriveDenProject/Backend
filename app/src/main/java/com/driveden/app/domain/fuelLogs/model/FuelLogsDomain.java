package com.driveden.app.domain.fuelLogs.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelLogsDomain {

    private Long id;
    private BigDecimal priceTotal;
    private BigDecimal pricePerGallon;
    private BigDecimal gallons;
    private Integer kmAtFill;
    private LocalDateTime filledAt;
    private String gasStation;
    private Long vehicleId;
    private Long paymentMethodId;
    private String notes;


}
