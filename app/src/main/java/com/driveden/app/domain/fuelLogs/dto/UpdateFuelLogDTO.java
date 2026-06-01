package com.driveden.app.domain.fuelLogs.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFuelLogDTO {

    @NotNull(message = "gallons is required")
    @DecimalMin(value = "0.01", message = "gallons must be greater than 0")
    private BigDecimal gallons;

    @NotNull(message = "priceTotal is required")
    @DecimalMin(value = "0.01", message = "priceTotal must be greater than 0")
    private BigDecimal priceTotal;

    @NotNull(message = "pricePerGallon is required")
    @DecimalMin(value = "0.01", message = "pricePerGallon must be greater than 0")
    private BigDecimal pricePerGallon;

    @NotNull(message = "kmAtFill is required")
    @PositiveOrZero(message = "kmAtFill must be zero or positive")
    private Integer kmAtFill;

    @NotNull(message = "filledAt is required")
    private LocalDateTime filledAt;

    @NotBlank(message = "gasStation is required")
    @Size(max = 100, message = "gasStation must be at most 100 characters")
    private String gasStation;

    @JsonProperty("payment_method_id")
    @Positive(message = "payment_method_id must be positive")
    private Long paymentMethodId;

    private String notes;
}
