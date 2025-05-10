package com.example.jigsawpuzzle.room.controller;

import com.example.jigsawpuzzle.domain.Room;
import com.example.jigsawpuzzle.room.service.RoomCreationFacade;
import com.example.jigsawpuzzle.room.service.RoomService;
import com.example.jigsawpuzzle.room.dto.RoomRequest;
import com.example.jigsawpuzzle.room.dto.RoomResponse;
import com.example.jigsawpuzzle.room.repository.projection.RoomWithoutPassword;
import com.example.jigsawpuzzle.security.JwtAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;
    private final RoomCreationFacade roomCreationFacade;

    public RoomController(RoomService roomService, RoomCreationFacade roomCreationFacade) {
        this.roomService = roomService;
        this.roomCreationFacade = roomCreationFacade;
    }

    @Operation(summary = "Tạo phòng", description = "Tạo một phòng mới, có thể kèm ảnh")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tạo phòng thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ")
    })
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @Parameter(description = "Ảnh tạo puzzle", required = false)
            @RequestParam(name = "image", required = false) MultipartFile image,
            @RequestBody RoomRequest roomRequest,
            JwtAuthentication jwtAuthentication
            ) throws IOException {
        RoomResponse room = roomCreationFacade.createRoom(image, roomRequest, jwtAuthentication);
        return ResponseEntity.ok(room);
    }

    @Operation(summary = "Lấy danh sách tất cả phòng (không kèm theo mật khẩu)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Danh sách phòng được trả về thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy phòng nào")
    })
    @GetMapping
    public ResponseEntity<List<RoomWithoutPassword>> getAllRooms() {
        List<RoomWithoutPassword> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @Operation(summary = "Tham gia một phòng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tham gia phòng thành công"),
            @ApiResponse(responseCode = "400", description = "Mật khẩu không chính xác"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy phòng")
    })
    @PostMapping("/{roomId}/enter")
    public ResponseEntity<RoomResponse> enterRoom(
            @PathVariable("roomId") Long roomId,
            @RequestBody String password,
            Authentication authentication) {
        RoomResponse room = this.roomService.enterRoom(roomId, password, authentication);
        return ResponseEntity.ok(room);
    }

    @Operation(summary = "Bắt đầu một phòng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phòng đã bắt đầu thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy phòng"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ")
    })
    @PostMapping("/{roomId}/start")
    public ResponseEntity<Void> startRoom(
            @RequestBody RoomRequest roomRequest,
            @PathVariable("roomId") Long roomId) throws IOException {
        roomService.startRoom(roomId, roomRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Rời khỏi một phòng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rời khỏi phòng thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy phòng"),
            @ApiResponse(responseCode = "400", description = "Người dùng không ở trong phòng")
    })
    @PatchMapping("/{roomId}/exit")
    public ResponseEntity<Void> exitRoom(
            @PathVariable("roomId") Long roomId,
            Authentication authentication) {
        roomService.exitRoom(roomId, authentication);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Đánh dấu người chơi đã sẵn sàng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Người chơi đã được đánh dấu là sẵn sàng"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy phòng"),
            @ApiResponse(responseCode = "400", description = "Người dùng không phải là người chơi trong phòng")
    })
    @PatchMapping("/{roomId}/ready")
    public ResponseEntity<Void> ready(
            @PathVariable("roomId") Long roomId,
            Authentication authentication) {
        roomService.ready(roomId, authentication);
        return ResponseEntity.ok().build();
    }
}
