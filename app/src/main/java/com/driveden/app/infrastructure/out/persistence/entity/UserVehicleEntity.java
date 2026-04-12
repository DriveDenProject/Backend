package com.driveden.app.infrastructure.out.persistence.entity;

import com.driveden.app.infrastructure.out.persistence.entity.ids.UserVehicleId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_vehicles")
@Getter
@Setter
@NoArgsConstructor
public class UserVehicleEntity {

    @EmbeddedId
    private UserVehicleId id;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @ManyToOne
    @MapsId("vehicleId")
    @JoinColumn(name = "vehicle_id")
    private VehicleEntity vehicle;
}