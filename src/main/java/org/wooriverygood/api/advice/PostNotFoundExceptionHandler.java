package org.wooriverygood.api.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.wooriverygood.api.exception.PostNotFoundException;

@ControllerAdvice
public class PostNotFoundExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<String> handle() {
        return ResponseEntity.badRequest().body("게시글을 찾을 수 없습니다.");
    }
}
