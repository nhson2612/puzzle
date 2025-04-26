package com.example.jigsawpuzzle.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    @OneToOne
    @JoinColumn(name = "match_id")
    private Match match;
    @OneToOne
    @JoinColumn(name = "puzzle_id")
    private Puzzle puzzle;
    private LocalDateTime savedAt;
}
