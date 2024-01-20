package org.wooriverygood.api.advice.exception.general;

public class BadRequestException extends BusinessException {

    public BadRequestException(String message) {
        super(message);
    }
}