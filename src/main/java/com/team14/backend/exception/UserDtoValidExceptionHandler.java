package com.team14.backend.exception;

import com.team14.backend.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //json형태로 body 반환
public class UserDtoValidExceptionHandler {

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleApiRequestException(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldError().getDefaultMessage();
        ResponseDto restApiException = new ResponseDto("failed", msg,"");

        return new ResponseEntity<>(restApiException, HttpStatus.BAD_REQUEST);
    }
}