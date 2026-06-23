package com.driveden.app.domain.cars.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class vehicleDomain {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private String nickName;
    private VehicleType vehicleType;
}
