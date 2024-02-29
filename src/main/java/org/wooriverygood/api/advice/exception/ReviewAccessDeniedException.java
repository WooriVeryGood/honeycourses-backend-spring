package org.wooriverygood.api.advice.exception;

import org.wooriverygood.api.advice.exception.general.BadRequestException;

public class ReviewAccessDeniedException extends BadRequestException {

    private static final String MESSAGE = "최근 6개월 동안 리뷰를 작성하지 않았습니다.";

    public ReviewAccessDeniedException() {
        super(MESSAGE);
    }
}
