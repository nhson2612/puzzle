package com.example.jigsawpuzzle.match.strategy;

import com.example.jigsawpuzzle.domain.Room;
import com.example.jigsawpuzzle.room.dto.RoomRequest;
import com.example.jigsawpuzzle.room.repository.RoomRepository;
import org.springframework.stereotype.Component;

@Component
public class OneVsOneMatchStarterStrategy implements MatchStarterStrategy {

    private final RoomRepository roomRepository;

    public OneVsOneMatchStarterStrategy(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void validate(Room room, RoomRequest request) {
        Long roomId = room.getId();
        int playerCount = roomRepository.countPlayersInRoom(roomId);
        int readyCount = roomRepository.countReadyPlayersInRoom(roomId);

        if (playerCount != 2) {
            throw new IllegalStateException("1v1 mode requires exactly 2 players.");
        }
        if (readyCount != 2) {
            throw new IllegalStateException("Both players must be ready in 1v1 mode.");
        }

        room.setJoinable(false);
    }
}

