package org.wooriverygood.api.global.error.exception;

import org.wooriverygood.api.global.error.exception.ForbiddenException;

public class AuthorizationException extends ForbiddenException {

    private static final String MESSAGE = "권한이 없습니다.";

    public AuthorizationException() {
        super(MESSAGE);
    }
}