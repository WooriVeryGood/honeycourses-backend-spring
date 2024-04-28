package org.wooriverygood.api.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.wooriverygood.api.global.ErrorResponse;
import org.wooriverygood.api.global.error.exception.BadRequestException;
import org.wooriverygood.api.global.error.exception.ForbiddenException;
import org.wooriverygood.api.global.error.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(e.getMessage(), new ArrayList<>()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage(), new ArrayList<>()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage(), new ArrayList<>()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        final BindingResult bindingResult = e.getBindingResult();
        final List<FieldError> errors = bindingResult.getFieldErrors();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildFieldErrors("Invalid input value", errors.stream()
                        .map(error -> ErrorResponse.FieldError.builder()
                                .reason(error.getDefaultMessage())
                                .field(error.getField())
                                .value((String) error.getRejectedValue())
                                .build())
                        .toList()));
    }

    private ErrorResponse buildFieldErrors(String message, List<ErrorResponse.FieldError> errors) {
        return ErrorResponse.builder()
                .message(message)
                .errors(errors)
                .build();
    }

}
