package com.numble.instagram.support.file;

import com.numble.instagram.exception.badrequest.NotImageFileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileStoreImplTest {

    @Autowired
    private FileStoreImpl fileStore;

    @Test
    @DisplayName("이미지파일은 업로드 되고 이미지 Url을 반환한다.")
    void uploadImageWithImageFile() {
        MultipartFile imageFile = new MockMultipartFile(
                "test.jpg", "test.jpg", "image/jpeg", "test.jpg".getBytes());

        String imageUrl = fileStore.uploadImage(imageFile);

        assertNotNull(imageUrl);
        System.out.println("imageUrl = " + imageUrl);
        assertTrue(imageUrl.startsWith("https://numble/"));
        assertTrue(imageUrl.endsWith("?alt=media"));
    }

    @Test
    @DisplayName("이미지 파일이 null 이면 디폴트 이미지를 반환한다.")
    void testUploadImageWithNullFile() {
        MultipartFile imageFile = null;

        String imageUrl = fileStore.uploadImage(imageFile);

        assertEquals("https://defaultImage.jpg", imageUrl);
    }

    @Test
    @DisplayName("이미지 파일이 아니면 NotImageFileException이 발생한다.")
    void testUploadImageWithNonImageFile() {
        MultipartFile imageFile = new MockMultipartFile("test.txt", "test.txt",
                "text/plain", "test.txt".getBytes());

        assertThrows(NotImageFileException.class, () -> fileStore.uploadImage(imageFile));
    }

    @Test
    @DisplayName("디폴트 이미지면 파일이 삭제되지 않는다.")
    void testDeleteFileWithDefaultImage() {
        String imageUrl = "https://defaultImage.jpg";

        fileStore.deleteFile(imageUrl);
    }
}