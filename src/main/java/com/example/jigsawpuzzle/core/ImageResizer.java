package com.example.jigsawpuzzle.core;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;

@Component
public class ImageResizer {

    public static final Map<Integer,Integer> EDGE_SIZES = Map.of(
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

    public byte[] resizeImage(byte[] imageBytes, int rows, int cols) throws IOException {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException("File không được null hoặc rỗng.");
        }
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Rows và cols phải lớn hơn 0.");
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        BufferedImage inputImage = ImageIO.read(bais);
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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "png", baos);
        return baos.toByteArray();
    }
}