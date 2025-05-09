package com.example.jigsawpuzzle.puzzle.repository;

import com.example.jigsawpuzzle.domain.PuzzlePiece;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class RedisPuzzlePieceRepository implements PuzzlePieceRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String REDIS_KEY_PREFIX = "match:"; // hoặc lấy từ @Value nếu cần

    public RedisPuzzlePieceRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    private String getKey(Long matchId) {
        return REDIS_KEY_PREFIX + matchId + ":pieces";
    }
    @Override
    public void saveAll(Long matchId, List<PuzzlePiece> pieces) {
        redisTemplate.opsForValue().set(getKey(matchId), pieces);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PuzzlePiece> findAllByMatchId(Long matchId) {
        Object value = redisTemplate.opsForValue().get(getKey(matchId));
        if (value instanceof List<?>) {
            return (List<PuzzlePiece>) value;
        }
        return Collections.emptyList();
    }
    @Override
    public void updatePiece(Long matchId, PuzzlePiece updatedPiece) {
        List<PuzzlePiece> pieces = findAllByMatchId(matchId);
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).getId().equals(updatedPiece.getId())) {
                pieces.set(i, updatedPiece);
                saveAll(matchId, pieces);
                return;
            }
        }
    }
}
