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
@Table(name = "repairs")
public class RepairEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "repair_date", nullable = false)
    private LocalDateTime repairDate;

    @Column(name = "description")
    private String description;

    @Column(name = "workshop", length = 150)
    private String workshop;

    @Column(name = "labor_cost")
    private BigDecimal laborCost;

    @Column(name = "total_cost")
    private BigDecimal totalCost;
}
