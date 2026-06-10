package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driveden.app.infrastructure.out.persistence.entity.PartCategoryEntity;
import com.driveden.app.infrastructure.out.persistence.projection.PartCategoryProjection;

public interface PartCategoryJpa extends JpaRepository<PartCategoryEntity, Long> {

    @Query("""
            SELECT pc.id AS id, pc.name AS name
            FROM PartCategoryEntity pc
            ORDER BY pc.name ASC
            """)
    List<PartCategoryProjection> findAllProjectedOrderByNameAsc();

    @Query("SELECT pc.id FROM PartCategoryEntity pc WHERE pc.id IN :ids")
    List<Long> findExistingIds(@Param("ids") Set<Long> ids);
}
