package com.numble.instagram.presentation;

import com.numble.instagram.dto.response.ErrorResponse;
import com.numble.instagram.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonControllerAdvice {

    private static final String LOG_FORMAT = "Body={}";

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customException(CustomException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(statusCode)
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        log.info(LOG_FORMAT, body, e);
        return ResponseEntity.status(statusCode).body(body);
    }
}
