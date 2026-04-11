package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driveden.app.infrastructure.out.persistence.entity.TransmissionTypesEntity;

public interface TransmissionTypeJpa extends JpaRepository<TransmissionTypesEntity, Long> {

    List<TransmissionTypesEntity> findAll();

}
