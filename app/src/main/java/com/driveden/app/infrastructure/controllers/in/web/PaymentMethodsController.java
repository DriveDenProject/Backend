package com.driveden.app.infrastructure.controllers.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driveden.app.application.services.PaymentMethodService;
import com.driveden.app.domain.paymentMethods.dto.PaymentMethodResponseDTO;
import com.driveden.app.utils.CustomResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment-methods")
@Validated
public class PaymentMethodsController {

    private final PaymentMethodService paymentMethodService;

    @GetMapping("/available")
    public CustomResponse<List<PaymentMethodResponseDTO>> getAvailablePaymentMethods() {
        return new CustomResponse<>(
                paymentMethodService.getAvailablePaymentMethods(),
                HttpStatus.OK,
                "Payment methods retrieved successfully"
        );
    }

}
