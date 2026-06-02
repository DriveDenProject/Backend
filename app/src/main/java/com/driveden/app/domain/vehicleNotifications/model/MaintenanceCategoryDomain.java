package com.driveden.app.domain.vehicleNotifications.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceCategoryDomain {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
