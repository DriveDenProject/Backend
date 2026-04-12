package com.driveden.app.domain.cars.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class carRegisterRequestDTO {

    private String nickName;
    private String carBrand;
    private String carModel;
    private Integer carYear;

    private Long fuelId;
    private Long transmissionId;

    private Integer current_km;
    private LocalDate last_technical_inspection;
    private LocalDate last_soat;

}
