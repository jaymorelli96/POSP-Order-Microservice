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
import org.springframework.web.server.ServerWebInputException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<?> handleException(ServerWebInputException e) {
        System.out.println("error");
        String error = e.getMessage();


        ValidationException apiException = new ValidationException(
                                        error,
                                        HttpStatus.UNPROCESSABLE_ENTITY,
                                        ZonedDateTime.now()
                                    );
        return ResponseEntity.unprocessableEntity().body(apiException);
    }

}