package com.driveden.app.application.ports.out;

import java.util.List;

import com.driveden.app.domain.repairs.model.RepairHistoryDomain;
import com.driveden.app.domain.repairs.model.RepairDomain;
import com.driveden.app.domain.repairs.model.RepairStatsDomain;

public interface RepairRepositoryPort {

    RepairDomain save(RepairDomain repair);

    List<RepairHistoryDomain> findHistoryByVehicleId(Long vehicleId);

    RepairStatsDomain findStatsByVehicleId(Long vehicleId);
}
