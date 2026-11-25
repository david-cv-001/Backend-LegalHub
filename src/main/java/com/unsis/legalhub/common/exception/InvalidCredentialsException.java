package com.unsis.legalhub.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiException {

    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "Unauthorized", "Invalid credentials");
    }
}
