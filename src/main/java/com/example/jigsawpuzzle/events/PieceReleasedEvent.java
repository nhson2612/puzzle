package com.example.jigsawpuzzle.events;

import com.example.jigsawpuzzle.dto.ReleasePieceRequest;

public class PieceReleasedEvent {
    private Long matchId;
    private Long pieceId;

    public PieceReleasedEvent(Long matchId, ReleasePieceRequest request) {
    }
}
