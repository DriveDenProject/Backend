package com.driveden.app.domain.odometerLogs.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OdometerLogDomain {

    private Long id;
    private Long vehicleId;
    private Integer km;
    private LocalDateTime recordedAt;
    private String note;
    private OdometerLogSource source;
    private Long sourceId;
    private LocalDateTime createdAt;
}
