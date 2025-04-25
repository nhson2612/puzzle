package com.example.jigsawpuzzle.core;

import com.example.jigsawpuzzle.config.PuzzleConfig;

import java.util.Random;

public class EdgeMapGenerator {
    private final int[][] topEdge, rightEdge, bottomEdge, leftEdge;
    private final Random random = new Random();

    public EdgeMapGenerator() {
        int n = PuzzleConfig.PIECE_COUNT;
        topEdge = new int[n][n];
        rightEdge = new int[n][n];
        bottomEdge = new int[n][n];
        leftEdge = new int[n][n];
    }

    public PuzzlePiece[][] generateEdges() {
        int n = PuzzleConfig.PIECE_COUNT;
        PuzzlePiece[][] pieces = new PuzzlePiece[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                topEdge[i][j] = i == 0 ? 0 : -bottomEdge[i - 1][j];
                leftEdge[i][j] = j == 0 ? 0 : -rightEdge[i][j - 1];
                bottomEdge[i][j] = (i == n - 1) ? 0 : (random.nextBoolean() ? 1 : -1);
                rightEdge[i][j] = (j == n - 1) ? 0 : (random.nextBoolean() ? 1 : -1);

                int x = j * PuzzleConfig.PIECE_SIZE;
                int y = i * PuzzleConfig.PIECE_SIZE;
                pieces[i][j] = new PuzzlePiece(i, j,
                        topEdge[i][j], rightEdge[i][j],
                        bottomEdge[i][j], leftEdge[i][j], x, y);
            }
        }
        return pieces;
    }
}

