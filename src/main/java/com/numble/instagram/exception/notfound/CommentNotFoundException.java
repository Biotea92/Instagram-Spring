package com.numble.instagram.exception.notfound;

public class CommentNotFoundException extends NotFoundException {

    public CommentNotFoundException() {
        addValidation("comment", "comment를 찾을 수 없습니다.");
    }
}
