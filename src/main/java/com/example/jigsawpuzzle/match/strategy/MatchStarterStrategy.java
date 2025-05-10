package com.example.jigsawpuzzle.match.strategy;

import com.example.jigsawpuzzle.domain.Room;
import com.example.jigsawpuzzle.room.dto.RoomRequest;

public interface MatchStarterStrategy {
    void validate(Room room, RoomRequest request);
}