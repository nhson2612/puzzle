package com.example.jigsawpuzzle.websocket.event;

import com.example.jigsawpuzzle.domain.Position;
import com.example.jigsawpuzzle.websocket.dto.MovePieceRequest;

public class PieceMovedEvent {
    private Long matchId;
    private Long pieceId;
    private Position newPosition;
    private Long userId;

    public PieceMovedEvent(Long matchId, MovePieceRequest request){

    }
}