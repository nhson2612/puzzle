package com.example.jigsawpuzzle.room.dto;

import com.example.jigsawpuzzle.domain.MatchMode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Thông tin yêu cầu để tạo hoặc bắt đầu phòng")
public record RoomRequest(
        @Schema(description = "Tên phòng", example = "Phòng chiến đấu")
        String roomName,
        @Schema(description = "Số người tối đa", example = "4")
        Integer maxPlayers,
        @Schema(description = "Mật khẩu (nếu có)", example = "123456")
        String pass,
        @Schema(description = "ID ảnh puzzle", example = "abc123")
        String imageId,
        @Schema(description = "Thời lượng trận đấu (tính bằng giây)", example = "300")
        Integer duration,
        @Schema(description = "Chế độ chơi", example = "ONEVSONE")
        MatchMode mode,
        @Schema(description = "Số hàng", example = "4")
        Integer rows,
        @Schema(description = "Số cột", example = "4")
        Integer cols
) { }
