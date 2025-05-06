package com.example.jigsawpuzzle.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class PlayerState {
    private Long userId;
    private List<PuzzlePiece> piecesHeld;
    private Boolean isConnected;
    private LocalDateTime lastActive;
    private List<PuzzlePiece> piecesPlacedCorrectly;
}