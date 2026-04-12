package com.driveden.app.domain.users.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVehicleDomain {

    private Long userId;
    private Long vehicleId;
    private Boolean isPrimary;
}
