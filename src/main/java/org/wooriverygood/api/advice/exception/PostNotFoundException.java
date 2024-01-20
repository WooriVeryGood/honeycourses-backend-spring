package org.wooriverygood.api.advice.exception;

import org.wooriverygood.api.advice.exception.general.NotFoundException;

public class PostNotFoundException extends NotFoundException {

    private static final String MESSAGE = "게시물을 찾을 수 없습니다.";


    public PostNotFoundException() {
        super(MESSAGE);
    }

}
