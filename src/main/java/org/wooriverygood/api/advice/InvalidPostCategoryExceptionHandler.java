package org.wooriverygood.api.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.wooriverygood.api.exception.InvalidPostCategoryException;

@ControllerAdvice
public class InvalidPostCategoryExceptionHandler {

    @ExceptionHandler(InvalidPostCategoryException.class)
    public ResponseEntity<String> handle() {
        return ResponseEntity.badRequest().body("게시글의 카테고리가 유효하지 않습니다.");
    }
}
