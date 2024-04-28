package org.wooriverygood.api.post.exception;

import org.wooriverygood.api.global.error.exception.BadRequestException;

public class InvalidPostContentException extends BadRequestException {

    private static final String MESSAGE = "게시글 내용이 유효하지 않습니다.";

    public InvalidPostContentException() {
        super(MESSAGE);
    }
}
