package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.driveden.app.domain.fuelType.model.FuelTypeDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.FuelTypeMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.FuelTypeJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FuelTypeRepository {

    private final FuelTypeJpa fuelTypeJpa;

    public List<FuelTypeDomain> findAll() {
        return fuelTypeJpa.findAll().stream()
                .map(FuelTypeMapper::toDomain)
                .toList();
    }

}
