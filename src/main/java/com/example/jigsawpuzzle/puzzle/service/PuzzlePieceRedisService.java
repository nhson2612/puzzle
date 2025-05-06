package com.example.jigsawpuzzle.puzzle.service;

import com.example.jigsawpuzzle.domain.PuzzlePiece;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PuzzlePieceRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${app.piece.prefix}")
    private static String REDIS_KEY_PREFIX;

    public PuzzlePieceRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public void savePuzzlePieces(Long matchId, List<PuzzlePiece> pieces) {
        String key = REDIS_KEY_PREFIX + matchId + ":pieces";
        redisTemplate.opsForValue().set(key, pieces);
    }
    @SuppressWarnings("unchecked")
    public List<PuzzlePiece> getPuzzlePieces(Long matchId) {
        String key = REDIS_KEY_PREFIX + matchId + ":pieces";
        Object value = redisTemplate.opsForValue().get(key);
        if (value instanceof List<?>) {
            return (List<PuzzlePiece>) value;
        }
        return Collections.emptyList();
    }
    public void updatePiece(Long matchId, PuzzlePiece updatedPiece) {
        List<PuzzlePiece> pieces = getPuzzlePieces(matchId);
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).getId().equals(updatedPiece.getId())) {
                pieces.set(i, updatedPiece);
                savePuzzlePieces(matchId, pieces);
                return;
            }
        }
    }
}
