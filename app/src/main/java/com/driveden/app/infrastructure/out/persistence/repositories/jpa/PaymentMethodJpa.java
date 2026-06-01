package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driveden.app.infrastructure.out.persistence.entity.PaymentMethodsEntity;

public interface PaymentMethodJpa extends JpaRepository<PaymentMethodsEntity, Long> {

    List<PaymentMethodsEntity> findByIsActiveTrue();

}
