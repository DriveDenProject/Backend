package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.driveden.app.application.ports.out.PartRepositoryPort;
import com.driveden.app.domain.repairs.model.PartDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.PartMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.PartJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PartRepository implements PartRepositoryPort {

    private final PartJpa partJpa;

    @Override
    public List<PartDomain> findByNamesIgnoreCase(List<String> names) {
        return partJpa.findByNamesIgnoreCase(names).stream()
                .map(PartMapper::toDomain)
                .toList();
    }

    @Override
    public List<PartDomain> saveAll(List<PartDomain> parts) {
        return partJpa.saveAll(
                        parts.stream()
                                .map(PartMapper::toEntity)
                                .toList()
                )
                .stream()
                .map(PartMapper::toDomain)
                .toList();
    }
}
