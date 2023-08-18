package com.microshop.orderservice.exception;

import com.microshop.orderservice.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AppExceptionalHandler {

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<ResponseDto> appExceptionHandler(Exception ex) {
        ResponseDto error = new ResponseDto();
        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ResponseDto error = new ResponseDto();
        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage("Invalid Request Body.");
        error.setOutput(errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
