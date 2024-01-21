package org.wooriverygood.api.advice.exception.general;

public class ReviewNotFoundException extends NotFoundException {
    private static final String MESSAGE = "댓글을 찾을 수 없습니다.";


    public ReviewNotFoundException() {
        super(MESSAGE);
    }
}
