package com.tech.neo.bo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice()
public class GlobalExceptionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String string = ex.getBindingResult().getFieldError().toString();
        int defaultValue = string.lastIndexOf("default message");
        String substring = string.substring(defaultValue);
        LOGGER.error("Validation error: " + substring);
        return new ResponseEntity<>("Validation error: " + substring, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDBException(DataIntegrityViolationException ex) {
        LOGGER.error("Database error: This user already exists");
        return new ResponseEntity<>("Database error: This user already exists", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class, NotEnoughMoney.class})
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        LOGGER.error(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


}
