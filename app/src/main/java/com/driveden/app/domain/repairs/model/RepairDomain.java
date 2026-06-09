package com.driveden.app.domain.repairs.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepairDomain {

    private Long id;
    private Long vehicleId;
    private LocalDateTime repairDate;
    private String description;
    private String workshop;
    private BigDecimal laborCost;
    private BigDecimal totalCost;
}
