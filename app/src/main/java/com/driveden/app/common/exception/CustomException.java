package com.driveden.app.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    public CustomException(String message, HttpStatus status, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public CustomException(String message, HttpStatus status) {
        this(message, status, status.name());
    }

}
