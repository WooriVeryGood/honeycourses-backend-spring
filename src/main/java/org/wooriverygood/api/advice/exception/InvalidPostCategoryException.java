package org.wooriverygood.api.advice.exception;

import org.wooriverygood.api.advice.exception.general.BadRequestException;

public class InvalidPostCategoryException extends BadRequestException {

    private static final String MESSAGE = "유효한 카테고리가 아닙니다.";

    public InvalidPostCategoryException() {
        super(MESSAGE);
    }
}
