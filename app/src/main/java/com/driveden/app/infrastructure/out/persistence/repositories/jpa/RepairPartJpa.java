package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driveden.app.infrastructure.out.persistence.entity.RepairPartEntity;
import com.driveden.app.infrastructure.out.persistence.entity.ids.RepairPartId;

public interface RepairPartJpa extends JpaRepository<RepairPartEntity, RepairPartId> {
}
