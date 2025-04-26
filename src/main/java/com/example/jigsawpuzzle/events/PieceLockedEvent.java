package com.example.jigsawpuzzle.events;

import com.example.jigsawpuzzle.domain.Position;
import com.example.jigsawpuzzle.dto.LockPieceRequest;
import com.example.jigsawpuzzle.dto.MovePieceRequest;

public class PieceLockedEvent {
    private Long matchId;
    private Long pieceId;
    private Long userId;
    public PieceLockedEvent(Long matchId, LockPieceRequest request){
    }
}
