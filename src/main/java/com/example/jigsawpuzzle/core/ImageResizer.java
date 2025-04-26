package com.example.jigsawpuzzle.core;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Map;

@Component
public class ImageResizer {

    private static final Map<Integer,Integer> EDGE_SIZES = Map.of(
        5,160,
            6,133,
            7,113,
            8,100,
            10,80,
            12,66,
            15,53,
            16,50,
            20,40
    );

    public MultipartFile resizeImage(MultipartFile file, int rows, int cols) throws IOException {
        // Input validation
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được null hoặc rỗng.");
        }
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Rows và cols phải lớn hơn 0.");
        }
        BufferedImage inputImage = ImageIO.read(file.getInputStream());
        if (inputImage == null) {
            throw new IOException("Không thể đọc ảnh đầu vào.");
        }

        Integer edgeSize = EDGE_SIZES.get(cols);
        if (edgeSize == null) {
            throw new IllegalArgumentException("Không tìm thấy kích thước mảnh phù hợp cho cols = " + cols);
        }

        int newWidth = edgeSize * cols;
        int newHeight = edgeSize * rows;
        Image scaledImage = inputImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        return convertBufferedImageToMultipartFile(resizedImage);
    }


    private MultipartFile convertBufferedImageToMultipartFile(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            byte[] byteArray = baos.toByteArray();

            return new MultipartFile() {
                @Override
                public String getName() {
                    return "image";
                }

                @Override
                public String getOriginalFilename() {
                    return "resized_image.png";
                }

                @Override
                public String getContentType() {
                    return "image/png";
                }

                @Override
                public boolean isEmpty() {
                    return byteArray.length == 0;
                }

                @Override
                public long getSize() {
                    return byteArray.length;
                }

                @Override
                public byte[] getBytes() throws IOException {
                    return byteArray;
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return new ByteArrayInputStream(byteArray);
                }

                @Override
                public void transferTo(File dest) throws IOException, IllegalStateException {
                    try (FileOutputStream fos = new FileOutputStream(dest)) {
                        fos.write(byteArray);
                    }
                }
            };
        }
    }
}