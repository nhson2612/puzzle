package com.example.jigsawpuzzle.core;

import java.util.Random;

public class EdgeMapGenerator {
    private final int[][] topEdge, rightEdge, bottomEdge, leftEdge;
    private final Random random = new Random();

    public EdgeMapGenerator(PuzzleConfig puzzleConfig) {
        int rows = puzzleConfig.ROWS;
        int cols = puzzleConfig.COLS;
        topEdge = new int[rows][cols];
        rightEdge = new int[rows][cols];
        bottomEdge = new int[rows][cols];
        leftEdge = new int[rows][cols];
    }

    public PuzzlePiece[][] generateEdges(PuzzleConfig puzzleConfig) {
        int rows = puzzleConfig.ROWS;
        int cols = puzzleConfig.COLS;
        PuzzlePiece[][] pieces = new PuzzlePiece[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                topEdge[i][j] = i == 0 ? 0 : -bottomEdge[i - 1][j];
                leftEdge[i][j] = j == 0 ? 0 : -rightEdge[i][j - 1];
                bottomEdge[i][j] = (i == rows - 1) ? 0 : (random.nextBoolean() ? 1 : -1);
                rightEdge[i][j] = (j == cols - 1) ? 0 : (random.nextBoolean() ? 1 : -1);

                int x = j * puzzleConfig.PIECE_SIZE;
                int y = i * puzzleConfig.PIECE_SIZE;
                pieces[i][j] = new PuzzlePiece(i, j,
                        topEdge[i][j], rightEdge[i][j],
                        bottomEdge[i][j], leftEdge[i][j], x, y);
            }
        }
        return pieces;
    }
}

