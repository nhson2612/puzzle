package com.example.jigsawpuzzle.core;

import com.example.jigsawpuzzle.core.mask.MaskApplier;
import com.example.jigsawpuzzle.core.mask.MaskFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class PuzzleGenerator {
    private final MaskApplier maskApplier;

    public PuzzleGenerator(String grpcHost, int grpcPort) {
        this.maskApplier = new MaskApplier(grpcHost, grpcPort);
    }

    public BufferedImage[] generatePieces(BufferedImage input, int rows, int cols) throws IOException {
        PuzzleConfig puzzleConfig = new PuzzleConfig(input.getWidth(),input.getHeight(),rows,cols);
        EdgeMapGenerator edgeMap = new EdgeMapGenerator(puzzleConfig);
        PuzzlePiece[][] pieces = edgeMap.generateEdges(puzzleConfig);

        BufferedImage[] outputs = new BufferedImage[puzzleConfig.ROWS * puzzleConfig.COLS];
//        BufferedImage[] outputs = new BufferedImage[PuzzleConfig.ROWS * PuzzleConfig.COLS];
        int index = 0;

        // Convert BufferedImage to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(input, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        for (PuzzlePiece[] row : pieces) {
            for (PuzzlePiece piece : row) {
                List<Integer> mask = MaskFactory.createMaskFor(piece);
                byte[] resultBytes = maskApplier.applyMask(
                        imageBytes,
                        mask,
                        piece.x,
                        piece.y,
                        puzzleConfig
                );
                outputs[index++] = ImageIO.read(new ByteArrayInputStream(resultBytes));
            }
        }
        return outputs;
    }
}