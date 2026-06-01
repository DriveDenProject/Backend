package com.driveden.app.infrastructure.out.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "fuel_logs")
public class FuelLogsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "price_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceTotal;

    @Column(name = "price_per_gallon", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerGallon;

    @Column(name = "gallons", nullable = false, precision = 10, scale = 2)
    private BigDecimal gallons;

    @Column(name = "km_at_fill", nullable = false)
    private Integer kmAtFill;

    @Column(name = "filled_at", nullable = false)
    private LocalDateTime filledAt;

    @Column(name = "gas_station", length = 100)
    private String gasStation;

    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @Column(name = "notes")
    private String notes;
}
