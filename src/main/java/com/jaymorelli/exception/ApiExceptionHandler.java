package com.jaymorelli.exception;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<?> handleException(WebExchangeBindException e) {
        List<String> errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());


        ValidationException apiException = new ValidationException(
                                        errors,
                                        HttpStatus.UNPROCESSABLE_ENTITY,
                                        ZonedDateTime.now()
                                    );
        return ResponseEntity.unprocessableEntity().body(apiException);
    }

}