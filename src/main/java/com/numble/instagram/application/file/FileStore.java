package com.numble.instagram.application.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStore {

    String uploadImage(MultipartFile imageFile);

    void deleteFile(String imageUrl);
}
