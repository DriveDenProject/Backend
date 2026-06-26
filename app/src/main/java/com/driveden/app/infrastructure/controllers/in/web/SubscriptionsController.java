package com.driveden.app.infrastructure.controllers.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driveden.app.application.services.SubscriptionService;
import com.driveden.app.domain.auth.dto.AuthenticatedUser;
import com.driveden.app.domain.subscriptions.dto.ActivateSubscriptionRequestDTO;
import com.driveden.app.domain.subscriptions.dto.ActivateSubscriptionResponseDTO;
import com.driveden.app.domain.subscriptions.dto.CurrentSubscriptionResponseDTO;
import com.driveden.app.domain.subscriptions.dto.SubscriptionPlanResponseDTO;
import com.driveden.app.utils.CustomResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscriptions")
@Validated
public class SubscriptionsController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/me")
    public CustomResponse<CurrentSubscriptionResponseDTO> getCurrentSubscription(Authentication authentication) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                subscriptionService.getCurrentSubscription(authenticatedUser.id()),
                HttpStatus.OK,
                "Current subscription retrieved successfully"
        );
    }

    @GetMapping("/plans")
    public CustomResponse<List<SubscriptionPlanResponseDTO>> getAvailablePlans() {
        return new CustomResponse<>(
                subscriptionService.getAvailablePlans(),
                HttpStatus.OK,
                "Subscription plans retrieved successfully"
        );
    }

    @PostMapping("/activate")
    public CustomResponse<ActivateSubscriptionResponseDTO> activate(
            @Valid @RequestBody ActivateSubscriptionRequestDTO request
    ) {
        return new CustomResponse<>(
                subscriptionService.activate(request),
                HttpStatus.NOT_IMPLEMENTED,
                "Subscription activation is not implemented yet"
        );
    }
}
