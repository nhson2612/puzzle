package com.example.jigsawpuzzle.room.dto;

import com.example.jigsawpuzzle.domain.MatchMode;
import com.example.jigsawpuzzle.domain.Room;
import com.example.jigsawpuzzle.domain.RoomStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Schema(description = "Thông tin trả về khi thao tác với phòng chơi")
public class RoomResponse {
    @Schema(description = "ID của phòng", example = "1")
    private Long id;
    @Schema(description = "Tên phòng", example = "Phòng vui vẻ")
    private String name;
    @Schema(description = "Người tạo phòng")
    private UserProjection host;
    @Schema(description = "Danh sách người chơi trong phòng")
    private List<RoomPlayerProjection> roomPlayers;
    @Schema(description = "Trận đấu hiện tại (nếu có)")
    private MatchProjection currentMatch;
    @Schema(description = "Thời gian tạo phòng", example = "2025-05-09T10:15:30")
    private LocalDateTime createdAt;
    @Schema(description = "Trạng thái phòng", example = "READY")
    private RoomStatus status;
    @Schema(description = "Số người chơi tối đa", example = "4")
    private Integer maxPlayers;
    @Schema(description = "Phòng có yêu cầu mật khẩu không", example = "true")
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
