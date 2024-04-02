package org.wooriverygood.api.comment.exception;

import org.wooriverygood.api.global.error.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {

    private static final String MESSAGE = "댓글을 찾을 수 없습니다.";


    public CommentNotFoundException() {
        super(MESSAGE);
    }

}
