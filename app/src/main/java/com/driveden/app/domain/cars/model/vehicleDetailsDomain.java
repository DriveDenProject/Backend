package com.driveden.app.domain.cars.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class vehicleDetailsDomain {
    private Long id;   
    private Long vehicleId;
    private Long fuelTypeId;
    private Long transmissionTypeId;
    private Integer currentKm;
    private LocalDate lastTechnicalInspection;
    private LocalDate lastSoat;
}
