package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.driveden.app.domain.paymentMethods.model.PaymentMethodDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.PaymentMethodMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.PaymentMethodJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PaymentMethodRepository {

    private final PaymentMethodJpa paymentMethodJpa;

    public List<PaymentMethodDomain> findAvailablePaymentMethods() {
        return paymentMethodJpa.findByIsActiveTrue().stream()
                .map(PaymentMethodMapper::toDomain)
                .toList();
    }

}
