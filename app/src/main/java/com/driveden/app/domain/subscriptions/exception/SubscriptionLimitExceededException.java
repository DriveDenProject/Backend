package com.driveden.app.domain.subscriptions.exception;

import org.springframework.http.HttpStatus;

import com.driveden.app.common.exception.CustomException;

public class SubscriptionLimitExceededException extends CustomException {

    public SubscriptionLimitExceededException(String message) {
        super(message, HttpStatus.FORBIDDEN, "SUBSCRIPTION_LIMIT_EXCEEDED");
    }
}
