package com.example.jigsawpuzzle.events;

import com.example.jigsawpuzzle.domain.Position;
import com.example.jigsawpuzzle.dto.MovePieceRequest;

public class PieceMovedEvent {
    private Long matchId;
    private Long pieceId;
    private Position newPosition;
    private Long userId;

    public PieceMovedEvent(Long matchId, MovePieceRequest request){

    }
}