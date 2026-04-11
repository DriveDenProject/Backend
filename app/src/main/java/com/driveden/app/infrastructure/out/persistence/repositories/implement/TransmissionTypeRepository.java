package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.driveden.app.domain.transmissionType.model.transmissionTypeDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.TransmissionTypeMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.TransmissionTypeJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TransmissionTypeRepository {

    private final TransmissionTypeJpa transmissionTypeJpa;

    public List<transmissionTypeDomain> findAll() {
        return transmissionTypeJpa.findAll().stream()
                .map(TransmissionTypeMapper::toDomain)
                .toList();
    }

    
}
