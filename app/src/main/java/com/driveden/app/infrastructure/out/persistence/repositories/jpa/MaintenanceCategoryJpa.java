package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driveden.app.infrastructure.out.persistence.entity.MaintenanceCategoryEntity;

public interface MaintenanceCategoryJpa extends JpaRepository<MaintenanceCategoryEntity, Long> {
}
