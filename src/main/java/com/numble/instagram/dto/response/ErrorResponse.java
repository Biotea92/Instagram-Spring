package com.numble.instagram.dto.response;

import lombok.Builder;

import java.util.Map;

public record ErrorResponse(int code, String message, Map<String, String> validation) {

    @Builder
    public ErrorResponse(int code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : Map.of();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
