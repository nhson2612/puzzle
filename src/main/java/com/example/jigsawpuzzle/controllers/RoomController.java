package com.example.jigsawpuzzle.controllers;

import com.example.jigsawpuzzle.domain.Room;
import com.example.jigsawpuzzle.dto.RoomRequest;
import com.example.jigsawpuzzle.security.JwtAuthentication;
import com.example.jigsawpuzzle.services.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(
            @RequestParam(name = "image", required = false)MultipartFile image,
            @RequestBody RoomRequest roomRequest,
            JwtAuthentication jwtAuthentication
            ) throws IOException {
        Room room = roomService.createRoom(image, roomRequest,jwtAuthentication);
        return ResponseEntity.ok(room);
    }
}
