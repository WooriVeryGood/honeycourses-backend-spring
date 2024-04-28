package org.wooriverygood.api.post.exception;

import org.wooriverygood.api.global.error.exception.BadRequestException;

public class InvalidPostTitleException extends BadRequestException {

    private static final String MESSAGE = "게시글 제목이 유효하지 않습니다.";

    public InvalidPostTitleException() {
        super(MESSAGE);
    }
}
