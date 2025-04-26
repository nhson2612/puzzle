package com.example.jigsawpuzzle.core;

public class PuzzleConfig {
    public int PIECE_SIZE;
    public int TAB_SIZE;
//    public int MASK_SIZE;
    public int ROWS;
    public int COLS;

    public PuzzleConfig(int width, int height, int rows, int cols) {
        if (width % cols != 0) {
            throw new IllegalArgumentException("Width must be divisible by the number of columns.");
        }
        if (height % rows != 0) {
            throw new IllegalArgumentException("Height must be divisible by the number of rows.");
        }

        // Tính toán kích thước các mảnh ghép
        this.PIECE_SIZE = width / cols;
        this.COLS = cols;
        this.ROWS = rows;
        this.TAB_SIZE = PIECE_SIZE / 3;
//        this.MASK_SIZE = PIECE_SIZE + (TAB_SIZE * 2);
    }
}
