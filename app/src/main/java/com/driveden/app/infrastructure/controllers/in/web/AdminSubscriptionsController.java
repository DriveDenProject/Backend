package com.driveden.app.infrastructure.controllers.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driveden.app.application.services.SubscriptionService;
import com.driveden.app.domain.subscriptions.dto.AdminGrantSubscriptionRequestDTO;
import com.driveden.app.domain.subscriptions.dto.CurrentSubscriptionResponseDTO;
import com.driveden.app.utils.CustomResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/subscriptions")
@Validated
public class AdminSubscriptionsController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/grant")
    public CustomResponse<CurrentSubscriptionResponseDTO> grantSubscription(
            @Valid @RequestBody AdminGrantSubscriptionRequestDTO request
    ) {
        return new CustomResponse<>(
                subscriptionService.grantSubscription(request.userId(), request.planCode()),
                HttpStatus.OK,
                "Subscription granted successfully"
        );
    }
}
