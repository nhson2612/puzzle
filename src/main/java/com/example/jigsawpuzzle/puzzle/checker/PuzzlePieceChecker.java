package com.example.jigsawpuzzle.puzzle.checker;

import com.example.jigsawpuzzle.domain.PuzzlePiece;
import com.example.jigsawpuzzle.puzzle.service.PuzzlePieceRedisService;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PuzzlePieceChecker {
    private final PuzzlePieceRedisService puzzlePieceRedisService;
    public PuzzlePieceChecker(PuzzlePieceRedisService puzzlePieceRedisService) {
        this.puzzlePieceRedisService = puzzlePieceRedisService;
    }
    public boolean canMove(Long matchId, Long pieceId, Long userId) {
        PuzzlePiece piece = getPieceById(matchId, pieceId);
        return piece != null && (piece.getHeldBy() == null || piece.getHeldBy().equals(userId));
    }
    public boolean canLock(Long matchId, Long pieceId, Long userId) {
        PuzzlePiece piece = getPieceById(matchId, pieceId);
        return piece != null && piece.getHeldBy() == null;
    }
    public boolean canRelease(Long matchId, Long pieceId, Long userId) {
        PuzzlePiece piece = getPieceById(matchId, pieceId);
        return piece != null && piece.getHeldBy() != null && piece.getHeldBy().equals(userId);
    }
    private PuzzlePiece getPieceById(Long matchId, Long pieceId) {
        List<PuzzlePiece> pieces = puzzlePieceRedisService.getPuzzlePieces(matchId);
        return pieces.stream()
                .filter(p -> p.getId().equals(pieceId))
                .findFirst()
                .orElse(null);
    }
}