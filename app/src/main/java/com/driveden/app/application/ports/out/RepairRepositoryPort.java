package com.driveden.app.application.ports.out;

import com.driveden.app.domain.repairs.model.RepairDomain;

public interface RepairRepositoryPort {

    RepairDomain save(RepairDomain repair);
}
