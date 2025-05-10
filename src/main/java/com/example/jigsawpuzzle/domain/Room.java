package com.example.jigsawpuzzle.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Builder
@AllArgsConstructor
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private User host;
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<RoomPlayer> roomPlayers;
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Match> matches;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // Thêm cascade
    @JoinColumn(name = "current_match_id")
    @JsonManagedReference
    private Match currentMatch;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    @Enumerated(EnumType.STRING)
    private RoomStatus status;
    private Integer maxPlayers;
    private String password;
    private Boolean joinable;

    public Room() {
    }

    public void addPlayer(RoomPlayer roomPlayer) {
        if (this.roomPlayers == null) {
            this.roomPlayers = new ArrayList<>();
        }
        if (this.roomPlayers.size() >= this.maxPlayers) {
            throw new IllegalStateException("Room is full");
        }
        boolean alreadyInRoom = this.roomPlayers.stream()
                .anyMatch(p -> p.getUser().getId().equals(roomPlayer.getUser().getId()));

        if (alreadyInRoom) {
            throw new IllegalStateException("User already in room");
        }
        this.roomPlayers.add(roomPlayer);
        roomPlayer.setRoom(this);
    }
    public void removePlayer(RoomPlayer player) {
        this.roomPlayers.remove(player);
        player.setRoom(null);
    }
}