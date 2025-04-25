package com.example.jigsawpuzzle.services;

import com.example.jigsawpuzzle.config.PuzzleConfig;
import com.example.jigsawpuzzle.core.PuzzleGenerator;
import com.example.jigsawpuzzle.mask.MaskApplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class PuzzleService {

    @Value("${puzzle.grpc.host:localhost}")
    private String grpcHost;

    @Value("${puzzle.grpc.port:50051}")
    private int grpcPort;

    @Value("${puzzle.storage.path:./uploads/puzzles}")
    private String storagePath;

    @Autowired
    private ImageService imageService;

    /**
     * Tạo các mảnh ghép puzzle từ ảnh đã tải lên
     * @param imageUrl URL của ảnh đã tải lên
     * @return Danh sách URL của các mảnh ghép
     */
    public List<String> generatePuzzlePieces(String imageUrl) throws IOException {
        // Tải ảnh từ URL
        byte[] imageBytes = imageService.getImageBytes(imageUrl);
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

        // Tạo ID duy nhất cho bộ puzzle này
        String puzzleId = UUID.randomUUID().toString();
        String puzzleDir = storagePath + "/" + puzzleId;
        Files.createDirectories(Paths.get(puzzleDir));

        // Lưu ảnh gốc
        Path originalPath = Paths.get(puzzleDir, "original.png");
        ImageIO.write(originalImage, "png", originalPath.toFile());

        // Tạo các mảnh ghép puzzle
        PuzzleGenerator generator = new PuzzleGenerator(grpcHost, grpcPort);
        BufferedImage[] puzzlePieces = generator.generatePieces(originalImage);
        List<String> pieceUrls = new ArrayList<>();

        // Lưu từng mảnh ghép
        for (int i = 0; i < puzzlePieces.length; i++) {
            String pieceFilename = String.format("piece_%d.png", i);
            Path piecePath = Paths.get(puzzleDir, pieceFilename);

            // Chuyển BufferedImage thành byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(puzzlePieces[i], "png", baos);
            byte[] pieceBytes = baos.toByteArray();

            // Lưu file
            Files.write(piecePath, pieceBytes);

            // Thêm URL vào danh sách kết quả
            pieceUrls.add("/puzzles/" + puzzleId + "/" + pieceFilename);
        }

        return pieceUrls;
    }
    public List<String> getPuzzlePiecesByImageId(String imageId) throws IOException {
        Path puzzleDir = Paths.get(storagePath, imageId);
        if (!Files.exists(puzzleDir)) {
            throw new IOException("Puzzle not found with ID: " + imageId);
        }

        List<String> pieceUrls = new ArrayList<>();
        Files.list(puzzleDir)
                .filter(path -> path.getFileName().toString().startsWith("piece_"))
                .forEach(path -> {
                    pieceUrls.add("/puzzles/" + imageId + "/" + path.getFileName().toString());
                });

        return pieceUrls;
    }
    public byte[] getPreviewImage(String imageId) throws IOException {
        Path previewPath = Paths.get(storagePath, imageId, "preview.png");
        if (!Files.exists(previewPath)) {
            throw new IOException("Preview image not found for puzzle ID: " + imageId);
        }

        return Files.readAllBytes(previewPath);
    }
    public byte[] getPuzzlePiece(String puzzleId, String pieceFilename) throws IOException {
        Path piecePath = Paths.get(storagePath, puzzleId, pieceFilename);
        if (!Files.exists(piecePath)) {
            throw new IOException("Puzzle piece not found: " + pieceFilename);
        }

        return Files.readAllBytes(piecePath);
    }
}