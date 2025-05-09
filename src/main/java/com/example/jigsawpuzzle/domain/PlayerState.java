package com.example.jigsawpuzzle.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class PlayerState {
    private Long userId;
    private Boolean isConnected;
    private LocalDateTime lastActive;
    private Integer correctPieces;
    private Integer score;
}