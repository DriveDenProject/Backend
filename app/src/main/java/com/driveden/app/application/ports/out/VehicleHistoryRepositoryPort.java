package com.driveden.app.application.ports.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.driveden.app.domain.vehicleHistory.model.VehicleHistoryItemDomain;

public interface VehicleHistoryRepositoryPort {

    Page<VehicleHistoryItemDomain> findByVehicleId(Long vehicleId, Pageable pageable);
}
