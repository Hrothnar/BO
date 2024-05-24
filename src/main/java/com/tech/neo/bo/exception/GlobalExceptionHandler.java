package com.tech.neo.bo.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice()
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String string = ex.getBindingResult().getFieldError().toString();
        int defaultValue = string.lastIndexOf("default message");
        String substring = string.substring(defaultValue);
        return new ResponseEntity<>("Validation error: " + substring, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDBException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>("Database error: This user already exists", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class, NotEnoughMoney.class})
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
