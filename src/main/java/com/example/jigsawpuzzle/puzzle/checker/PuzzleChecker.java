package com.example.jigsawpuzzle.puzzle.checker;

import com.example.jigsawpuzzle.domain.PuzzlePiece;
import com.example.jigsawpuzzle.puzzle.repository.PuzzlePieceRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PuzzleChecker {
    private final PuzzlePieceRepository puzzlePieceRepository;

    public PuzzleChecker(PuzzlePieceRepository puzzlePieceRepository) {
        this.puzzlePieceRepository = puzzlePieceRepository;
    }

    public boolean canMove(PuzzlePiece piece,Long userId) {
        return piece != null && (piece.getHeldBy() == null || piece.getHeldBy().equals(userId));
    }
    public boolean canLock(PuzzlePiece piece, Long userId) {
        return piece != null && piece.getHeldBy() == null;
    }
    public boolean canRelease(PuzzlePiece piece, Long userId) {
        return piece != null && piece.getHeldBy() != null && piece.getHeldBy().equals(userId);
    }
    private PuzzlePiece getPieceById(Long matchId, Long pieceId) {
        List<PuzzlePiece> pieces = puzzlePieceRepository.findAllByMatchId(matchId);
        return pieces.stream()
                .filter(p -> p.getId().equals(pieceId))
                .findFirst()
                .orElse(null);
    }
}