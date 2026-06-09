package com.driveden.app.domain.repairs.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRepairPartDTO {

    @NotBlank(message = "name is required")
    @Size(max = 200, message = "name must be at most 200 characters")
    private String name;

    @NotNull(message = "categoryId is required")
    @Positive(message = "categoryId must be positive")
    private Long categoryId;

    @Size(max = 100, message = "brand must be at most 100 characters")
    private String brand;

    @NotNull(message = "quantity is required")
    @Positive(message = "quantity must be positive")
    private Integer quantity;

    @NotNull(message = "unitPrice is required")
    @DecimalMin(value = "0.01", message = "unitPrice must be greater than 0")
    private BigDecimal unitPrice;

    private LocalDate warrantyExpiration;

    private LocalDate partExpiration;
}
