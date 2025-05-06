package com.example.jigsawpuzzle.room.dto;

import com.example.jigsawpuzzle.domain.MatchMode;
import com.example.jigsawpuzzle.domain.Room;
import com.example.jigsawpuzzle.domain.RoomStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RoomResponse {
    private Long id;
    private String name;
    private UserProjection host;
    private List<RoomPlayerProjection> roomPlayers;
    private MatchProjection currentMatch;
    private LocalDateTime createdAt;
    private RoomStatus status;
    private Integer maxPlayers;
    private boolean requirePassword;

    public RoomResponse(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.host = new UserProjection(room.getHost().getId(), room.getHost().getUsername());
        this.roomPlayers = room.getRoomPlayers() == null ? List.of() :
                room.getRoomPlayers().stream()
                        .map(rp -> new RoomPlayerProjection(rp.getUser().getId()))
                        .collect(Collectors.toList());
        this.currentMatch = room.getCurrentMatch() == null ? null :
                new MatchProjection(
                        room.getCurrentMatch().getId(),
                        new PuzzleProjection(
                                room.getCurrentMatch().getPuzzle().getOriginalImageUrl(),
                                room.getCurrentMatch().getPuzzle().getNumberOfPieces(),
                                room.getCurrentMatch().getPuzzle().getRowCount(),
                                room.getCurrentMatch().getPuzzle().getColCount()
                        ),
                        room.getCurrentMatch().getMode()
                );
        this.createdAt = room.getCreatedAt();
        this.status = room.getStatus();
        this.maxPlayers = room.getMaxPlayers();
        this.requirePassword = room.getPassword() != null && !room.getPassword().isEmpty();
    }

    public record RoomPlayerProjection( Long userId) {}
    public record MatchProjection(Long id, PuzzleProjection puzzle, MatchMode mode) {}
    public record UserProjection(Long id, String username) {}
    public record PuzzleProjection(String originalImageUrl, Integer numberOfPieces, Integer rowCount, Integer colCount) {}
}
