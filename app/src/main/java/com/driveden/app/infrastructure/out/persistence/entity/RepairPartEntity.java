package com.driveden.app.infrastructure.out.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.driveden.app.infrastructure.out.persistence.entity.ids.RepairPartId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "repair_parts")
public class RepairPartEntity {

    @EmbeddedId
    private RepairPartId id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @Column(name = "warranty_expiration")
    private LocalDate warrantyExpiration;

    @Column(name = "part_expiration")
    private LocalDate partExpiration;
}
