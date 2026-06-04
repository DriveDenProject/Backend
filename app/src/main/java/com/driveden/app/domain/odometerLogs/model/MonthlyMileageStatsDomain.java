package com.driveden.app.domain.odometerLogs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyMileageStatsDomain {

    private String month;
    private Integer kmTraveled;
}
