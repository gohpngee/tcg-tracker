package com.tcgtracker.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tcgtracker.common.exception.classes.NoCardsFoundException;

@RestControllerAdvice 
public class GlobalExceptionHandler {
    @ExceptionHandler(NoCardsFoundException.class)
    public ResponseEntity<String> handleNoCardsFoundException(NoCardsFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    
}
