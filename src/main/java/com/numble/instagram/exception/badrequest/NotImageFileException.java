package com.numble.instagram.exception.badrequest;

public class NotImageFileException extends InvalidRequestException {

    public NotImageFileException() {
        addValidation("multipartFile", "이미지 파일이 아닙니다.");
    }
}
