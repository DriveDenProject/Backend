package com.driveden.app.domain.subscriptions.exception;

import org.springframework.http.HttpStatus;

import com.driveden.app.common.exception.CustomException;

public class InvalidSubscriptionStateException extends CustomException {

    public InvalidSubscriptionStateException(String message) {
        super(message, HttpStatus.CONFLICT, "INVALID_SUBSCRIPTION_STATE");
    }
}
