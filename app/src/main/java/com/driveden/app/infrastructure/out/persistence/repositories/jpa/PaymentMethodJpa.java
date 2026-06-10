package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.driveden.app.infrastructure.out.persistence.entity.PaymentMethodsEntity;
import com.driveden.app.infrastructure.out.persistence.projection.PaymentMethodProjection;

public interface PaymentMethodJpa extends JpaRepository<PaymentMethodsEntity, Long> {

    @Query("""
            SELECT pm.id AS id, pm.name AS name
            FROM PaymentMethodsEntity pm
            WHERE pm.isActive = true
            """)
    List<PaymentMethodProjection> findAvailableProjected();

    boolean existsByIdAndIsActiveTrue(Long id);

}
