package com.example.jigsawpuzzle.controllers;

import com.example.jigsawpuzzle.domain.PuzzlePiece;
import com.example.jigsawpuzzle.dto.*;
import com.example.jigsawpuzzle.events.*;
import com.example.jigsawpuzzle.factories.PuzzlePieceFactory;
import com.example.jigsawpuzzle.services.PuzzlePieceChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Slf4j
public class WebSocketController {

    private final PuzzlePieceFactory puzzlePieceFactory;
    private final PuzzlePieceChecker puzzlePieceChecker;

    public WebSocketController(PuzzlePieceFactory puzzlePieceFactory, PuzzlePieceChecker puzzlePieceChecker) {
        this.puzzlePieceFactory = puzzlePieceFactory;
        this.puzzlePieceChecker = puzzlePieceChecker;
    }

    // Init game
    @MessageMapping("/match/{matchId}/init")
    @SendTo("/topic/match/{matchId}/init")
    public InitGameEvent initGame(@DestinationVariable Long matchId, InitGameRequest request) {
        log.info("Init match: {}", matchId);
        List<PuzzlePiece> pieces = puzzlePieceFactory.createInitialPieces(matchId);
        return new InitGameEvent(matchId, pieces);
    }

    // Move piece
    @MessageMapping("/match/{matchId}/move")
    @SendTo("/topic/match/{matchId}/move")
    public PieceMovedEvent movePiece(@DestinationVariable Long matchId, MovePieceRequest request) {
        boolean canMove = puzzlePieceChecker.canMove(request.pieceId());
        log.info("Handle moving piece: {} (canMove: {})", request.pieceId(), canMove);
        return new PieceMovedEvent(matchId, request);
    }

    // Lock piece
    @MessageMapping("/match/{matchId}/lock")
    @SendTo("/topic/match/{matchId}/lock")
    public PieceLockedEvent lockPiece(@DestinationVariable Long matchId, LockPieceRequest request) {
        boolean canLock = puzzlePieceChecker.canLock(request.pieceId());
        log.info("Handle locking piece: {} (canLock: {})", request.pieceId(), canLock);
        return new PieceLockedEvent(matchId, request);
    }

    // Release piece
    @MessageMapping("/match/{matchId}/release")
    @SendTo("/topic/match/{matchId}/release")
    public PieceReleasedEvent releasePiece(@DestinationVariable Long matchId, ReleasePieceRequest request) {
        boolean canRelease = puzzlePieceChecker.canRelease(request.pieceId());
        log.info("Handle releasing piece: {} (canRelease: {})", request.pieceId(), canRelease);
        return new PieceReleasedEvent(matchId, request);
    }

    // Sync all pieces state
    @MessageMapping("/match/{matchId}/sync")
    @SendTo("/topic/match/{matchId}/sync")
    public SyncStateEvent syncState(@DestinationVariable Long matchId) {
        log.info("Syncing match state for matchId: {}", matchId);
        List<PuzzlePiece> currentPieces = puzzlePieceFactory.getCurrentPieces(matchId);
        return new SyncStateEvent(matchId, currentPieces);
    }
}
