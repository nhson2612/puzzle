package com.example.jigsawpuzzle.core;

import com.example.jigsawpuzzle.core.ImageResizer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.*;
import java.util.Arrays;
import java.util.UUID;

@Service
public class ImageService {

    protected final String uploadDir = "uploads/images/";
    private final ImageResizer imageResizer;

    public ImageService(ImageResizer imageResizer) {
        this.imageResizer = imageResizer;
    }

    public String saveImage(byte[] fileBytes, String fileName) throws IOException {
        if (fileBytes == null || fileBytes.length == 0) {
            throw new IOException("Empty file");
        }
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String originalFilename = StringUtils.cleanPath(fileName);
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex).toLowerCase();
            if (!Arrays.asList(".jpg", ".jpeg", ".png").contains(extension)) {
                throw new IOException("Unsupported file format");
            }
        }
        String uniqueFileName = UUID.randomUUID().toString() + extension;
        Path filePath = uploadPath.resolve(uniqueFileName);
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(fileBytes);
        }
        if (!Files.exists(filePath) || Files.size(filePath) == 0) {
            throw new IOException("Failed to save file");
        }
        return "/images/" + uniqueFileName;
    }
    public byte[] getImageBytes(String imageUrl) throws IOException {
        String filename = Paths.get(URI.create(imageUrl).getPath()).getFileName().toString();
        Path imagePath = Paths.get(uploadDir).resolve(filename);
        if (!Files.exists(imagePath)) {
            throw new IOException("Image not found: " + filename);
        }
        return Files.readAllBytes(imagePath);
    }
    public byte[] getImageById(String id) throws IOException {
        Path imagePath = Paths.get(uploadDir).resolve(id );
        if (!Files.exists(imagePath)) {
            throw new IOException("Image not found for ID: " + id);
        }
        return Files.readAllBytes(imagePath);
    }
    public String resolvePuzzleImageUrl(MultipartFile image, String imageId) throws IOException {
        if (imageId != null) {
            return "/images/" + imageId + ".png";
        } else if (image != null) {
            return saveImage(image.getBytes(), image.getOriginalFilename());
        }
        throw new IllegalArgumentException("No image provided");
    }

}
