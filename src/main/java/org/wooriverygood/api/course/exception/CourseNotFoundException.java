package org.wooriverygood.api.course.exception;

import org.wooriverygood.api.global.error.exception.NotFoundException;

public class CourseNotFoundException extends NotFoundException {
    private static final String MESSAGE = "강의를 찾을 수 없습니다.";

    public CourseNotFoundException() { super(MESSAGE); }

}