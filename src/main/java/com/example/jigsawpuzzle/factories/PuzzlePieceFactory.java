package com.example.jigsawpuzzle.factories;

import com.example.jigsawpuzzle.domain.PuzzlePiece;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PuzzlePieceFactory {
    public List<PuzzlePiece> createInitialPieces(Long matchId) {
        List<PuzzlePiece> pieces = new ArrayList<>();
        // TODO
        return pieces;
    }

    public List<PuzzlePiece> getCurrentPieces(Long matchId) {
        List<PuzzlePiece> pieces = new ArrayList<>();
        // TODO
        return pieces;
    }
}

