package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driveden.app.infrastructure.out.persistence.entity.PartCategoryEntity;

public interface PartCategoryJpa extends JpaRepository<PartCategoryEntity, Long> {

    @Query("SELECT pc.id FROM PartCategoryEntity pc WHERE pc.id IN :ids")
    List<Long> findExistingIds(@Param("ids") Set<Long> ids);
}
