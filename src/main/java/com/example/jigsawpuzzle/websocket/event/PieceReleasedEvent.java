package com.example.jigsawpuzzle.websocket.event;

import com.example.jigsawpuzzle.websocket.dto.ReleasePieceRequest;

public class PieceReleasedEvent {
    private Long matchId;
    private Long pieceId;

    public PieceReleasedEvent(Long matchId, ReleasePieceRequest request) {
    }
}
