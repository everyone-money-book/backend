package com.team14.backend.exception;

import com.team14.backend.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {
    @ExceptionHandler(value = {CustomErrorException.class})
    public ResponseEntity<Object> handleApiRequestException(RuntimeException ex) {
        ResponseDto restApiException = new ResponseDto("failed",ex.getMessage(),"");

        return new ResponseEntity<>(
                restApiException,
                HttpStatus.OK
        );
    }
}