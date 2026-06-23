package com.driveden.app.domain.cars.dto;

import java.time.LocalDate;

import com.driveden.app.domain.cars.model.VehicleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class carRegisterRequestDTO {

    private String nickName;
    @NotBlank(message = "carBrand is required")
    private String carBrand;
    @NotBlank(message = "carModel is required")
    private String carModel;
    @NotNull(message = "carYear is required")
    @Positive(message = "carYear must be positive")
    private Integer carYear;
    @NotNull(message = "vehicleType is required")
    private VehicleType vehicleType;

    @NotNull(message = "fuelId is required")
    @Positive(message = "fuelId must be positive")
    private Long fuelId;
    @NotNull(message = "transmissionId is required")
    @Positive(message = "transmissionId must be positive")
    private Long transmissionId;

    @NotNull(message = "current_km is required")
    @PositiveOrZero(message = "current_km must be zero or positive")
    private Integer current_km;
    private LocalDate last_technical_inspection;
    private LocalDate last_soat;

}
