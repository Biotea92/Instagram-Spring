package com.numble.instagram.support.file;

import com.numble.instagram.exception.badrequest.NotImageFileException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
public class FileStoreImpl implements FileStore {

    private static final String PREFIX = "https://numble/";
    private static final String SUFFIX = "?alt=media";
    private static final String DEFAULT_IMAGE = "https://defaultImage.jpg";

    @Override
    public String uploadImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return DEFAULT_IMAGE;
        }
        validateContentTypeImage(imageFile);

        String originalFilename = imageFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        // TODO upload logic S3

        return "%s%s%s".formatted(PREFIX, storeFileName, SUFFIX);
    }

    @Override
    public void deleteFile(String imageUrl) {
        if (imageUrl.equals(DEFAULT_IMAGE)) {
            return;
        }
        // TODO delete login;
    }

    private static void validateContentTypeImage(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        if (contentType != null && !contentType.startsWith("image")) {
            throw new NotImageFileException();
        }
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
