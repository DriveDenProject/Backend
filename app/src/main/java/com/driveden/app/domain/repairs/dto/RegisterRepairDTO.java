package com.driveden.app.domain.repairs.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRepairDTO {

    @NotNull(message = "vehicleId is required")
    @Positive(message = "vehicleId must be positive")
    private Long vehicleId;

    @NotNull(message = "repairDate is required")
    private LocalDate repairDate;

    private String description;

    @Size(max = 150, message = "workshop must be at most 150 characters")
    private String workshop;

    @DecimalMin(value = "0.00", message = "laborCost must be zero or positive")
    private BigDecimal laborCost;

    @NotNull(message = "totalCost is required")
    @DecimalMin(value = "0.00", message = "totalCost must be zero or positive")
    private BigDecimal totalCost;

    @Valid
    @NotEmpty(message = "parts is required")
    @Size(max = 100, message = "parts must contain at most 100 items")
    private List<RegisterRepairPartDTO> parts;
}
