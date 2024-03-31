package org.wooriverygood.api.post.exception;

import org.wooriverygood.api.global.error.exception.NotFoundException;

public class PostNotFoundException extends NotFoundException {

    private static final String MESSAGE = "게시물을 찾을 수 없습니다.";


    public PostNotFoundException() {
        super(MESSAGE);
    }

}
