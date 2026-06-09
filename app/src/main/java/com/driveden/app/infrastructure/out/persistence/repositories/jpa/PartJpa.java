package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driveden.app.infrastructure.out.persistence.entity.PartEntity;

public interface PartJpa extends JpaRepository<PartEntity, Long> {

    @Query("SELECT p FROM PartEntity p WHERE LOWER(p.name) IN :names")
    List<PartEntity> findByNamesIgnoreCase(@Param("names") List<String> names);
}
