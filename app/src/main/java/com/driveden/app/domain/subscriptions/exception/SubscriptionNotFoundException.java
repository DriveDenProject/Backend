package com.driveden.app.domain.subscriptions.exception;

import org.springframework.http.HttpStatus;

import com.driveden.app.common.exception.CustomException;

public class SubscriptionNotFoundException extends CustomException {

    public SubscriptionNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "SUBSCRIPTION_NOT_FOUND");
    }
}
