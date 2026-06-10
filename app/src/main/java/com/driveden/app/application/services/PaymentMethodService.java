package com.driveden.app.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.driveden.app.application.ports.out.PaymentMethodRepositoryPort;
import com.driveden.app.domain.paymentMethods.dto.PaymentMethodResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepositoryPort paymentMethodRepository;

    public List<PaymentMethodResponseDTO> getAvailablePaymentMethods() {
        return paymentMethodRepository.findAvailablePaymentMethods().stream()
                .map(paymentMethod -> new PaymentMethodResponseDTO(
                        paymentMethod.getId(),
                        paymentMethod.getName()
                ))
                .toList();
    }

}
