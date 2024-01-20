package org.wooriverygood.api.advice.exception;

import org.wooriverygood.api.advice.exception.general.ForbiddenException;

public class AuthorizationException extends ForbiddenException {

    private static final String MESSAGE = "권한이 없습니다.";

    public AuthorizationException() {
        super(MESSAGE);
    }
}