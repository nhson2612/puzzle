package com.example.jigsawpuzzle.match.strategy;

import com.example.jigsawpuzzle.domain.Room;
import com.example.jigsawpuzzle.room.dto.RoomRequest;
import com.example.jigsawpuzzle.room.repository.RoomRepository;
import org.springframework.stereotype.Component;

@Component
public class TeamMatchStarterStrategy implements MatchStarterStrategy {

    private final RoomRepository roomRepository;

    public TeamMatchStarterStrategy(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void validate(Room room, RoomRequest request) {
        int readyCount = roomRepository.countReadyPlayersInRoom(room.getId());
        if (readyCount == 0) {
            throw new IllegalStateException("At least one player must be ready to start the match.");
        }

        room.setJoinable(true);
    }
}