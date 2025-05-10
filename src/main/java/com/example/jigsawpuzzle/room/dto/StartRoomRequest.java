package com.example.jigsawpuzzle.room.dto;

import com.example.jigsawpuzzle.domain.RoomPlayer;

public record StartRoomRequest(Long roomId, Integer duration, RoomPlayer roomPlayer,Integer rows) {
}
