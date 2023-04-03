package com.numble.instagram.exception.badrequest;

public class ImageFileNotExistsException extends InvalidRequestException {

    public ImageFileNotExistsException() {
        addValidation("multipartFile", "파일이 존재하지 않습니다.");
    }
}
