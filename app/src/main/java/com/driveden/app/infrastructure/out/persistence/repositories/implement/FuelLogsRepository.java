package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.driveden.app.domain.fuelLogs.model.FuelLogsDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.FuelLogsMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.FuelLogsJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FuelLogsRepository {

    private final FuelLogsJpa fuelLogsJpa;

    public FuelLogsDomain save(FuelLogsDomain fuelLogsDomain) {
        return FuelLogsMapper.toDomain(
                fuelLogsJpa.save(
                        FuelLogsMapper.toEntity(fuelLogsDomain)
                )
        );
    }

    public List<FuelLogsDomain> findByVehicleId(Long vehicleId) {
        return fuelLogsJpa.findByVehicleIdOrderByFilledAtDesc(vehicleId).stream()
                .map(FuelLogsMapper::toDomain)
                .toList();
    }
}
