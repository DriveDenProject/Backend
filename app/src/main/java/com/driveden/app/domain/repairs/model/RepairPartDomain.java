package com.driveden.app.domain.repairs.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepairPartDomain {

    private Long repairId;
    private Long partId;
    private Integer quantity;
    private BigDecimal cost;
    private LocalDate warrantyExpiration;
    private LocalDate partExpiration;
}
