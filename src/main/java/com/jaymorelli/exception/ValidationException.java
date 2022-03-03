package com.jaymorelli.exception;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ValidationException{
    
    private String errorMessages;
    private HttpStatus httpStatus;
    private ZonedDateTime timestap;

}
