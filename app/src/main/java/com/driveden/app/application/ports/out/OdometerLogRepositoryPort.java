package com.driveden.app.application.ports.out;

import com.driveden.app.domain.odometerLogs.model.OdometerLogDomain;

public interface OdometerLogRepositoryPort {

    OdometerLogDomain save(OdometerLogDomain odometerLogDomain);
}
