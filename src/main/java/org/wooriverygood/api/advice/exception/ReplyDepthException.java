package org.wooriverygood.api.advice.exception;

import org.wooriverygood.api.global.error.exception.BadRequestException;

public class ReplyDepthException extends BadRequestException {

    private static final String MESSAGE = "답글에 답글을 달 수 없습니다.";

    public ReplyDepthException() {
        super(MESSAGE);
    }
}
