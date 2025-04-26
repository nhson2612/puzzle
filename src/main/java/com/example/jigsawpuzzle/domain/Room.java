package com.example.jigsawpuzzle.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private User host;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puzzle_id")
    private Puzzle puzzle;
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomPlayer> roomPlayers;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    @Enumerated(EnumType.STRING)
    private RoomStatus status;
    private Integer maxPlayers;
    private String password;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;
}
