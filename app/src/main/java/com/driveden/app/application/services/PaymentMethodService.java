package com.driveden.app.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.driveden.app.domain.paymentMethods.model.PaymentMethodDomain;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.PaymentMethodRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public List<PaymentMethodDomain> getAvailablePaymentMethods() {
        return paymentMethodRepository.findAvailablePaymentMethods();
    }

}
