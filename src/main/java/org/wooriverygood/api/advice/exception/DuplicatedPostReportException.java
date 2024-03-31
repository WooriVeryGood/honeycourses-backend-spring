package org.wooriverygood.api.advice.exception;

import org.wooriverygood.api.global.error.exception.BadRequestException;

public class DuplicatedPostReportException extends BadRequestException {

    private static final String MESSAGE = "이미 신고한 게시글이에요.";

    public DuplicatedPostReportException() {
        super(MESSAGE);
    }
}
