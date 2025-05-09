package com.example.jigsawpuzzle.puzzle.service;

import com.example.jigsawpuzzle.domain.PlayerState;
import com.example.jigsawpuzzle.domain.Position;
import com.example.jigsawpuzzle.domain.PuzzlePiece;
import com.example.jigsawpuzzle.puzzle.checker.PuzzleChecker;
import com.example.jigsawpuzzle.puzzle.repository.PuzzlePieceRepository;
import com.example.jigsawpuzzle.scoring.ScoreCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class PuzzlePieceService {
    @Value("${app.puzzle-piece.threshold}")
    private Double THRESHOLD;
    private final PuzzlePieceRepository pieceRepository;
    private final PuzzleChecker puzzleChecker;
    private final SimpMessagingTemplate messagingTemplate;
    private final ScoreCalculator scoreCalculator;

    public PuzzlePieceService(PuzzlePieceRepository pieceRepository, PuzzleChecker puzzleChecker, SimpMessagingTemplate messagingTemplate, ScoreCalculator scoreCalculator) {
        this.pieceRepository = pieceRepository;
        this.puzzleChecker = puzzleChecker;
        this.messagingTemplate = messagingTemplate;
        this.scoreCalculator = scoreCalculator;
    }

    public void movePiece(Long matchId, Long pieceId, Long userId, Position position){
        List<PuzzlePiece> pieces = pieceRepository.findAllByMatchId(matchId);
        for (PuzzlePiece piece : pieces) {
            if (piece.getId().equals(pieceId)) {
                boolean canMove = puzzleChecker.canMove(piece, userId);
                if (canMove) {
                    piece.setCurrentPosition(new Position(position.getX(), position.getY()));
                    piece.setHeldBy(userId);
                    pieceRepository.updatePiece(matchId, piece);
                } else {
                    throw new IllegalStateException("User is not allowed to move this piece");
                }
                break;
            }
        }
    }

    public void lockPiece(Long matchId, Long pieceId, Long userId) {
        List<PuzzlePiece> pieces = pieceRepository.findAllByMatchId(matchId);

        for (PuzzlePiece piece : pieces) {
            if (piece.getId().equals(pieceId)) {
                if (!piece.isLocked()) {
                    boolean canLock = puzzleChecker.canLock(piece, userId);
                    if (canLock) {
                        piece.lock(userId);
                        pieceRepository.updatePiece(matchId, piece);
                        log.info("Piece {} has been locked by user {}", pieceId, userId);
                    }else {
                        throw new IllegalStateException("User is not allowed to lock this piece");
                    }
                } else {
                    log.warn("Piece {} is already locked by user {}", pieceId, piece.getHeldBy());
                }
                break;
            }
        }
    }
    public void releaseAndCheckPosition(Long matchId, Long pieceId, Long userId, Position droppedPosition) {
        List<PuzzlePiece> pieces = pieceRepository.findAllByMatchId(matchId);
        for (int i = 0; i < pieces.size(); i++) {
            PuzzlePiece piece = pieces.get(i);
            Boolean isPlacedCorrectly = piece.getIsPlacedCorrectly();
            if (piece.getId().equals(pieceId)) {
                boolean canRelease = puzzleChecker.canRelease(piece, userId);
                if (canRelease) {
                    piece.updatePositionWithSnap(droppedPosition, THRESHOLD);
                    if(!isPlacedCorrectly) {piece.release();}
                    pieceRepository.updatePiece(matchId, piece);
                    pieces.set(i, piece);
                    if(isPlacedCorrectly){
                        handlePlaceCorrectly(matchId,userId,piece,pieces);
                    }
                } else {
                    throw new IllegalStateException("User is not allowed to release this piece");
                }
                break;
            }
        }
        boolean allPlacedCorrectly = pieces.stream().allMatch(PuzzlePiece::getIsPlacedCorrectly);
        if (allPlacedCorrectly) {
            handlePuzzleCompleted(matchId);
        }
    }
    private void handlePuzzleCompleted(Long matchId) {
//        messagingTemplate.convertAndSend();
        log.info("Puzzle match {} has been completed!", matchId);
    }
    private void handlePlaceCorrectly(Long matchId,Long userId,PuzzlePiece piece,List<PuzzlePiece> pieces) {
        PlayerState state = this.scoreCalculator.calculateScore(piece, pieces,userId);
        this.messagingTemplate.convertAndSend(String.format("/match/%d/state", matchId), state);
        log.info("User {} placed piece {} correctly in match {}", userId, piece.getId(), matchId);

    }

}