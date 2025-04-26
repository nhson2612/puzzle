package com.example.jigsawpuzzle.core;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

@Component
public class ImageResizer {

    private List<Integer> ranges = List.of(60, 50, 40, 30);

    public MultipartFile resizeImage(MultipartFile file, int rows, int cols) throws IOException {
        BufferedImage inputImage = ImageIO.read(file.getInputStream());
        if (inputImage == null) {
            throw new IOException("Không thể đọc ảnh đầu vào.");
        }

        int bestRange = ranges.get(0);
        int minDifference = Integer.MAX_VALUE;
        for (int range : ranges) {
            int calculatedWidth = cols * range;
            int calculatedHeight = rows * range;
            int widthDifference = Math.abs(calculatedWidth - inputImage.getWidth());
            int heightDifference = Math.abs(calculatedHeight - inputImage.getHeight());
            int totalDifference = widthDifference + heightDifference;
            if (totalDifference < minDifference) {
                minDifference = totalDifference;
                bestRange = range;
            }
        }

        int targetWidth = cols * bestRange;
        int targetHeight = rows * bestRange;

        // Calculate scaling to preserve aspect ratio
        double widthRatio = (double) targetWidth / inputImage.getWidth();
        double heightRatio = (double) targetHeight / inputImage.getHeight();
        double scale = Math.min(widthRatio, heightRatio);

        int scaledWidth = (int) (inputImage.getWidth() * scale);
        int scaledHeight = (int) (inputImage.getHeight() * scale);

        // Resize while preserving aspect ratio
        BufferedImage resizedImage = resizeAndSaveImage(inputImage, scaledWidth, scaledHeight);

        // Create a new image with exact target dimensions, centering the resized image
        BufferedImage finalImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = finalImage.createGraphics();
        g2d.setColor(new Color(0, 0, 0, 0)); // Transparent background
        g2d.fillRect(0, 0, targetWidth, targetHeight);
        int xOffset = (targetWidth - scaledWidth) / 2;
        int yOffset = (targetHeight - scaledHeight) / 2;
        g2d.drawImage(resizedImage, xOffset, yOffset, scaledWidth, scaledHeight, null);
        g2d.dispose();

        return convertBufferedImageToMultipartFile(finalImage);
    }

    private BufferedImage resizeAndSaveImage(BufferedImage inputImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(inputImage, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
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