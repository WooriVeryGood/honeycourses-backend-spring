package org.wooriverygood.api.post.exception;

import org.wooriverygood.api.global.error.exception.BadRequestException;

public class InvalidPostCategoryException extends BadRequestException {

    private static final String MESSAGE = "유효한 카테고리가 아닙니다.";

    public InvalidPostCategoryException() {
        super(MESSAGE);
    }
}
