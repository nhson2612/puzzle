package com.example.jigsawpuzzle.websocket.event;

import com.example.jigsawpuzzle.domain.PuzzlePiece;

import java.util.List;

public class SyncStateEvent {
    private Status status; // SUCCESS, FAIL
    private String message;
    private Long matchId;
    private List<PuzzlePiece> pieces;
    public SyncStateEvent(Long matchId,List<PuzzlePiece> pieces){

    }
}