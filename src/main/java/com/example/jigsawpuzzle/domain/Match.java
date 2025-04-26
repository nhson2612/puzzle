package com.example.jigsawpuzzle.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class Match {
    @Id
    private UUID id;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "match_players",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> players;
    @Embedded()
    private MatchResult result;
    @Enumerated(EnumType.STRING)
    private MatchMode mode;
}
