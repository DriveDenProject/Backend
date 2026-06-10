package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.driveden.app.application.ports.out.PartCategoryRepositoryPort;
import com.driveden.app.domain.repairs.model.PartCategoryDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.PartCategoryMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.PartCategoryJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PartCategoryRepository implements PartCategoryRepositoryPort {

    private final PartCategoryJpa partCategoryJpa;

    @Override
    public List<PartCategoryDomain> findAllOrderByNameAsc() {
        return partCategoryJpa.findAllProjectedOrderByNameAsc().stream()
                .map(PartCategoryMapper::toDomain)
                .toList();
    }

    @Override
    public Set<Long> findExistingIds(Set<Long> categoryIds) {
        return partCategoryJpa.findExistingIds(categoryIds).stream()
                .collect(Collectors.toSet());
    }

    @Override
    public boolean existsById(Long categoryId) {
        return partCategoryJpa.existsById(categoryId);
    }
}
