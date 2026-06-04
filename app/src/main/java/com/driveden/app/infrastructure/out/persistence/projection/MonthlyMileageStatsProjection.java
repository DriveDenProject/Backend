package com.driveden.app.infrastructure.out.persistence.projection;

public interface MonthlyMileageStatsProjection {

    String getMonth();

    Integer getKmTraveled();
}
