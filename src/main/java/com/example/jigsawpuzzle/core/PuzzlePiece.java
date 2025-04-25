package com.example.jigsawpuzzle.core;

public class PuzzlePiece {
    public final int row, col;
    public final int top, right, bottom, left;
    public final int x, y;

    public PuzzlePiece(int row, int col, int top, int right, int bottom, int left, int x, int y) {
        this.row = row;
        this.col = col;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("Piece[%d,%d] T:%d R:%d B:%d L:%d Pos:(%d,%d)",
                row, col, top, right, bottom, left, x, y);
    }
}
