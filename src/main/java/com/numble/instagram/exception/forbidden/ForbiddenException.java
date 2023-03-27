package com.numble.instagram.exception.forbidden;

import com.numble.instagram.exception.CustomException;

public class ForbiddenException extends CustomException {

    private static final String MESSAGE = "접근 권한이 없습니다.";

    private static final int STATUS_CODE = 403;

    public ForbiddenException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return STATUS_CODE;
    }
}
