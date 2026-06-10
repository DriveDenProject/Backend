package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.driveden.app.application.ports.out.PaymentMethodRepositoryPort;
import com.driveden.app.domain.paymentMethods.model.PaymentMethodDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.PaymentMethodMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.PaymentMethodJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PaymentMethodRepository implements PaymentMethodRepositoryPort {

    private final PaymentMethodJpa paymentMethodJpa;

    @Override
    public List<PaymentMethodDomain> findAvailablePaymentMethods() {
        return paymentMethodJpa.findAvailableProjected().stream()
                .map(PaymentMethodMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsAvailableById(Long id) {
        return paymentMethodJpa.existsByIdAndIsActiveTrue(id);
    }

}
