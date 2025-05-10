package com.example.jigsawpuzzle.websocket.event;

import com.example.jigsawpuzzle.websocket.dto.LockPieceRequest;

public class PieceLockedEvent {
    private Long matchId;
    private Long pieceId;
    private Long userId;
    public PieceLockedEvent(Long matchId, LockPieceRequest request){
    }
}
