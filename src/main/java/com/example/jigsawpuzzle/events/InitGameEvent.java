package com.example.jigsawpuzzle.events;

import com.example.jigsawpuzzle.domain.PuzzlePiece;

import java.util.List;

public class InitGameEvent {
    private Status status; // SUCCESS, FAIL
    private String message;
    private Long matchId;
    private List<PuzzlePiece> pieces;

    public InitGameEvent(Long matchId,List<PuzzlePiece> pieces){

    }
}