package com.example.jigsawpuzzle.mask;

import com.example.jigsawpuzzle.core.PuzzlePiece;

import java.util.List;

public class MaskFactory {
    public static List<Integer> createMaskFor(PuzzlePiece piece) {
        // Tạo mask dựa trên các cạnh của mảnh ghép
        // Có thể mở rộng để tạo các dạng mask phức tạp hơn
        return List.of(
                piece.top,    // Top edge
                piece.right,  // Right edge
                piece.bottom, // Bottom edge
                piece.left    // Left edge
        );
    }

    // Phương thức mới để tạo mask chi tiết hơn
//    public static List<Integer> createDetailedMask(PuzzlePiece piece) {
//        // Tạo mask chi tiết với nhiều thông số hơn
//        return List.of(
//                piece.top, piece.right, piece.bottom, piece.left,
//                puz.PIECE_SIZE,
//                PuzzleConfig.TAB_SIZE,
//                piece.x, piece.y
//        );
//    }
}