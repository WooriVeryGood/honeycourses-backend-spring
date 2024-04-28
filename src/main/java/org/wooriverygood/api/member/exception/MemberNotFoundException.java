package org.wooriverygood.api.member.exception;

import org.wooriverygood.api.global.error.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {
    private static final String MESSAGE = "유저 정보를 찾을 수 없습니다.";


    public MemberNotFoundException() {
        super(MESSAGE);
    }
}
