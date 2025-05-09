package com.example.jigsawpuzzle.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game_sessions")
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
    @PrePersist
    public void onPrePersist() {
        savedAt = LocalDateTime.now();
    }
    @PreUpdate
    public void onPreUpdate() {
        savedAt = LocalDateTime.now();
    }
}
