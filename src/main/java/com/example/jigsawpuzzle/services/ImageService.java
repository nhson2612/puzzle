package com.example.jigsawpuzzle.services;

import com.example.jigsawpuzzle.core.ImageResizer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.*;
import java.util.Arrays;
import java.util.UUID;

@Service
public class ImageService {

    private final String uploadDir = "uploads/images/";
    private final ImageResizer imageResizer;

    public ImageService(ImageResizer imageResizer) {
        this.imageResizer = imageResizer;
    }

    public String saveImage(MultipartFile file) throws IOException {
        // Validate file
        if (file.isEmpty()) {
            throw new IOException("Empty file");
        }

        // Prepare directory
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Process filename
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex).toLowerCase();
            // Validate extension
            if (!Arrays.asList(".jpg", ".jpeg", ".png").contains(extension)) {
                throw new IOException("Unsupported file format");
            }
        }

        String uniqueFileName = UUID.randomUUID().toString() + extension;
        Path filePath = uploadPath.resolve(uniqueFileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        // Verify the saved file
        if (!Files.exists(filePath) || Files.size(filePath) == 0) {
            throw new IOException("Failed to save file");
        }

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(uniqueFileName)
                .toUriString();
    }
    public byte[] getImageBytes(String imageUrl) throws IOException {
        // Tách tên file từ URL
        String filename = Paths.get(URI.create(imageUrl).getPath()).getFileName().toString();

        // Tạo đường dẫn đầy đủ đến file
        Path imagePath = Paths.get(uploadDir).resolve(filename);

        // Kiểm tra sự tồn tại của file
        if (!Files.exists(imagePath)) {
            throw new IOException("Image not found: " + filename);
        }

        // Đọc và trả về mảng byte của ảnh
        return Files.readAllBytes(imagePath);
    }


}
