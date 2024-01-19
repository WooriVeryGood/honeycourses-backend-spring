package org.wooriverygood.api.exception;

import org.wooriverygood.api.exception.general.BusinessException;

public class InvalidPostCategoryException extends BusinessException {

    private static final String MESSAGE = "유효한 카테고리가 아닙니다.";

    public InvalidPostCategoryException() {
        super(MESSAGE);
    }
}
