package com.example.jigsawpuzzle.websocket.controller;

import com.example.jigsawpuzzle.domain.PuzzlePiece;
import com.example.jigsawpuzzle.puzzle.service.PuzzlePieceService;
import com.example.jigsawpuzzle.websocket.dto.InitGameRequest;
import com.example.jigsawpuzzle.websocket.dto.LockPieceRequest;
import com.example.jigsawpuzzle.websocket.dto.MovePieceRequest;
import com.example.jigsawpuzzle.websocket.dto.ReleasePieceRequest;
import com.example.jigsawpuzzle.puzzle.factory.PuzzlePieceFactory;
import com.example.jigsawpuzzle.match.MatchRepository;
import com.example.jigsawpuzzle.puzzle.checker.PuzzleChecker;
import com.example.jigsawpuzzle.websocket.event.*;
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
    private final PuzzleChecker puzzleChecker;
    private final PuzzlePieceService pieceService;
    private final MatchRepository matchRepository;

    public WebSocketController(PuzzlePieceFactory puzzlePieceFactory, PuzzleChecker puzzleChecker, PuzzlePieceService pieceService, MatchRepository matchRepository) {
        this.puzzlePieceFactory = puzzlePieceFactory;
        this.puzzleChecker = puzzleChecker;
        this.pieceService = pieceService;
        this.matchRepository = matchRepository;
    }

    //Get status
    @SubscribeMapping("/room/{matchId}/load-status")
    public MatchStatus subscribeToMatchLoadStatus(@DestinationVariable Long matchId) {
        MatchStatus status = matchRepository.findMatchStatusById(matchId);
        return status;
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
        pieceService.movePiece(matchId,request.pieceId(),request.userId(),request.newPosition());
        return new PieceMovedEvent(matchId, request);
    }

    // Lock piece
    @MessageMapping("/match/{matchId}/lock")
    @SendTo("/topic/match/{matchId}/lock")
    public PieceLockedEvent lockPiece(@DestinationVariable Long matchId, LockPieceRequest request) {
        pieceService.lockPiece(matchId,request.pieceId(),request.userId());
        return new PieceLockedEvent(matchId, request);
    }

    // Release piece
    @MessageMapping("/match/{matchId}/release")
    @SendTo("/topic/match/{matchId}/release")
    public PieceReleasedEvent releasePiece(@DestinationVariable Long matchId, ReleasePieceRequest request) {
        pieceService.releaseAndCheckPosition(matchId,request.pieceId(),request.userId(),request.position());
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