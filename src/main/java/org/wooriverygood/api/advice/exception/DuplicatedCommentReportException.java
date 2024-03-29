package org.wooriverygood.api.advice.exception;

import org.wooriverygood.api.advice.exception.general.BadRequestException;

public class DuplicatedCommentReportException extends BadRequestException {

    private static final String MESSAGE = "이미 신고한 댓글이에요.";

    public DuplicatedCommentReportException() {
        super(MESSAGE);
    }
}
