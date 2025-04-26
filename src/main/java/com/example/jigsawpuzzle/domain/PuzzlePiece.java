package com.example.jigsawpuzzle.domain;

import jakarta.annotation.PostConstruct;
public class PuzzlePiece {
    private Long id;
    private String imageUrl;
    private Position correctPosition;
    private Position currentPosition;
    private Boolean isPlacedCorrectly;
    private User heldBy;

    @PostConstruct
    public void checkPlacement() {
        this.isPlacedCorrectly = correctPosition.equals(currentPosition);
    }
}
