package com.example.jigsawpuzzle.match.strategy;

import com.example.jigsawpuzzle.domain.Room;
import com.example.jigsawpuzzle.room.dto.RoomRequest;

public class SoloStarter implements MatchStarterStrategy{
    @Override
    public void validate(Room room, RoomRequest request) {
        room.setJoinable(false);
    }
}
