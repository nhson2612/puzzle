package com.example.jigsawpuzzle.websocket.event;

import com.example.jigsawpuzzle.domain.PuzzlePiece;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InitGameEvent {
    private Status status;
    private String message;
    private Long matchId;
    private List<PuzzlePiece> pieces;

    public InitGameEvent(Long matchId, List<PuzzlePiece> pieces) {
        this.matchId = matchId;

        if (pieces == null || pieces.isEmpty()) {
            this.status = Status.FAIL;
            this.message = "Puzzle pieces are missing.";
            this.pieces = null;
        } else {
            this.status = Status.SUCCESS;
            this.message = "Game initialized successfully.";
            this.pieces = pieces;
        }
    }
}