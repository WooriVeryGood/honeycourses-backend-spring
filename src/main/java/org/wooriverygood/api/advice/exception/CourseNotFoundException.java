package org.wooriverygood.api.advice.exception;

import org.wooriverygood.api.advice.exception.general.NotFoundException;

public class CourseNotFoundException extends NotFoundException {
    private static final String MESSAGE = "강의를 찾을 수 없습니다.";

    public CourseNotFoundException() { super(MESSAGE); }

}