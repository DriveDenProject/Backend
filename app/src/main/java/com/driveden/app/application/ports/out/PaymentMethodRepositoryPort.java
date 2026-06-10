package com.driveden.app.application.ports.out;

import java.util.List;

import com.driveden.app.domain.paymentMethods.model.PaymentMethodDomain;

public interface PaymentMethodRepositoryPort {

    List<PaymentMethodDomain> findAvailablePaymentMethods();

    boolean existsAvailableById(Long id);
}
