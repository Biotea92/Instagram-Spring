package com.numble.instagram.dto.response;

import lombok.Builder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record ErrorResponse(int code, String message, Map<String, String> validation) {

    @Builder
    public ErrorResponse(int code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new ConcurrentHashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
