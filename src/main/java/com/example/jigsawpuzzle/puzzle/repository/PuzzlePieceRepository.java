package com.example.jigsawpuzzle.puzzle.repository;

import com.example.jigsawpuzzle.domain.PuzzlePiece;

import java.util.List;

public interface PuzzlePieceRepository {
    void saveAll(Long matchId, List<PuzzlePiece> pieces);
    List<PuzzlePiece> findAllByMatchId(Long matchId);
    void updatePiece(Long matchId, PuzzlePiece updatedPiece);
}
