package com.example.jigsawpuzzle.room.controller;

import com.example.jigsawpuzzle.domain.Room;
import com.example.jigsawpuzzle.room.service.RoomCreationFacade;
import com.example.jigsawpuzzle.room.service.RoomService;
import com.example.jigsawpuzzle.room.dto.RoomRequest;
import com.example.jigsawpuzzle.room.dto.RoomResponse;
import com.example.jigsawpuzzle.room.repository.projection.RoomWithoutPassword;
import com.example.jigsawpuzzle.security.JwtAuthentication;
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

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @RequestParam(name = "image", required = false)MultipartFile image,
            @RequestBody RoomRequest roomRequest,
            JwtAuthentication jwtAuthentication
            ) throws IOException {
        RoomResponse room = roomCreationFacade.createRoom(image, roomRequest,jwtAuthentication);
        return ResponseEntity.ok(room);
    }
    @GetMapping
    public ResponseEntity<List<RoomWithoutPassword>> getAllRooms() {
        List<RoomWithoutPassword> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }
    @PostMapping("/{roomId}/enter")
    public ResponseEntity<RoomResponse> enterRoom(@PathVariable("roomId")Long roomId,@RequestBody String password ,Authentication authentication){
        RoomResponse room = this.roomService.enterRoom(roomId, password,authentication);
        return ResponseEntity.ok(room);
    }

    @PostMapping("/{roomId}/start")
    public ResponseEntity<Void> startRoom(@RequestBody RoomRequest roomRequest, @PathVariable("roomId") Long roomId) throws IOException {
        roomService.startRoom(roomId, roomRequest);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{roomId}/exit")
    public ResponseEntity<Void> exitRoom(@PathVariable("roomId") Long roomId, Authentication authentication){
        roomService.exitRoom(roomId,authentication);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{roomId}/ready")
    public ResponseEntity<Void> ready(@PathVariable("roomId") Long roomId, Authentication authentication){
        roomService.ready(roomId,authentication);
        return ResponseEntity.ok().build();
    }
}
