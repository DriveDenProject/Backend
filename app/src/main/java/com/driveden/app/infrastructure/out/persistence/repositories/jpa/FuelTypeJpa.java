package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driveden.app.infrastructure.out.persistence.entity.FuelTypesEntity;

public interface FuelTypeJpa extends JpaRepository<FuelTypesEntity, Long> {

    List<FuelTypesEntity> findAll();

}
