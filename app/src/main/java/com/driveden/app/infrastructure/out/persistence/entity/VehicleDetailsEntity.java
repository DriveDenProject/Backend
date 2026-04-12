package com.driveden.app.infrastructure.out.persistence.entity;

import java.time.LocalDate;

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
@Table(name = "vehicle_details")
public class VehicleDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "fuel_type_id")
    private Long fuelTypeId;

    @Column(name = "transmission_type_id")
    private Long transmissionTypeId;

    @Column(name = "current_km")
    private Integer currentKm;

    @Column(name = "last_technical_inspection")
    private LocalDate lastTechnicalInspection;

    @Column(name = "last_soat")
    private LocalDate lastSoat;
}