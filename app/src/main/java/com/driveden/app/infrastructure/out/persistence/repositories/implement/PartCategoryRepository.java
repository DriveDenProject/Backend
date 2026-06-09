package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.driveden.app.application.ports.out.PartCategoryRepositoryPort;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.PartCategoryJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PartCategoryRepository implements PartCategoryRepositoryPort {

    private final PartCategoryJpa partCategoryJpa;

    @Override
    public Set<Long> findExistingIds(Set<Long> categoryIds) {
        return partCategoryJpa.findExistingIds(categoryIds).stream()
                .collect(Collectors.toSet());
    }
}
