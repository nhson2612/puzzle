package com.example.jigsawpuzzle.domain;

import jakarta.annotation.PostConstruct;
public class PuzzlePiece {
    private Long id;
    private String imageUrl;
    private Position correctPosition;
    private Position currentPosition;
    private Boolean isPlacedCorrectly;
    private Long heldBy;
    public void updatePositionWithSnap(Position droppedPosition, double threshold) {
        this.currentPosition = droppedPosition;
        if (this.correctPosition.distanceTo(droppedPosition) <= threshold) {
            this.currentPosition = this.correctPosition;
            this.isPlacedCorrectly = true;
        } else {
            this.isPlacedCorrectly = false;
        }
    }
    public boolean isLocked() {
        return this.heldBy != null;
    }
    public void lock(Long userId) {
        this.heldBy = userId;
    }
    public void release() {
        this.heldBy = null;
    }
}