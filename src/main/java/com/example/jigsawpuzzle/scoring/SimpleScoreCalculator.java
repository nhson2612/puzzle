package com.example.jigsawpuzzle.scoring;

import com.example.jigsawpuzzle.domain.PlayerState;
import com.example.jigsawpuzzle.domain.PuzzlePiece;

import java.time.LocalDateTime;
import java.util.List;

public class SimpleScoreCalculator implements ScoreCalculator {
    @Override
    public PlayerState calculateScore(PuzzlePiece puzzlePiece, List<PuzzlePiece> pieces,Long userId) {
        long totalUserPieces = pieces.stream()
                .filter(p -> userId.equals(p.getHeldBy()))
                .count();
        long correctUserPieces = pieces.stream()
                .filter(p -> userId.equals(p.getHeldBy()) && Boolean.TRUE.equals(p.getIsPlacedCorrectly()))
                .count();

        int score = totalUserPieces == 0 ? 0 : (int) ((double) correctUserPieces / totalUserPieces * 100);
        PlayerState state = new PlayerState();
        state.setUserId(userId);
        state.setScore(score);
        state.setCorrectPieces((int) correctUserPieces);
        state.setLastActive(LocalDateTime.now());
        state.setIsConnected(true);
        return state;
    }
}