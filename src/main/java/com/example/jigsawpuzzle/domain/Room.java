package com.example.jigsawpuzzle.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rooms")
@Builder
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private User host;
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RoomPlayer> roomPlayers;
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Match> matches;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // Thêm cascade
    @JoinColumn(name = "current_match_id")
    private Match currentMatch;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    @Enumerated(EnumType.STRING)
    private RoomStatus status;
    private Integer maxPlayers;
    private String password;

    public Room() {
    }
}


