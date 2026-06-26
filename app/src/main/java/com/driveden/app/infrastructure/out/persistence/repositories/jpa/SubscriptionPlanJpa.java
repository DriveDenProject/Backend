package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driveden.app.infrastructure.out.persistence.entity.SubscriptionPlanEntity;

public interface SubscriptionPlanJpa extends JpaRepository<SubscriptionPlanEntity, Long> {

    Optional<SubscriptionPlanEntity> findByCodeAndActiveTrue(String code);

    List<SubscriptionPlanEntity> findByActiveTrueOrderByMonthlyPriceAsc();

    @EntityGraph(attributePaths = "features")
    @Query("SELECT plan FROM SubscriptionPlanEntity plan WHERE plan.code = :code AND plan.active = true")
    Optional<SubscriptionPlanEntity> findActiveByCodeWithFeatures(@Param("code") String code);
}
