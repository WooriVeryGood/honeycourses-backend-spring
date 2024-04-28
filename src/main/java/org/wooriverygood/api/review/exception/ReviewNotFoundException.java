package org.wooriverygood.api.review.exception;

import org.wooriverygood.api.global.error.exception.NotFoundException;

public class ReviewNotFoundException extends NotFoundException {
    private static final String MESSAGE = "댓글을 찾을 수 없습니다.";


    public ReviewNotFoundException() {
        super(MESSAGE);
    }
}
