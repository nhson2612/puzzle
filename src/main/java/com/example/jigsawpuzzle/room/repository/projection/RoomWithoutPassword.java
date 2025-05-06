package com.example.jigsawpuzzle.room.repository.projection;

import com.example.jigsawpuzzle.domain.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomWithoutPassword {
    Long getId();
    String getName();
    UserProjection getHost();
    List<RoomPlayerProjection> getRoomPlayers();
    MatchProjection getCurrentMatch();
    LocalDateTime getCreatedAt();
    RoomStatus getStatus();
    Integer getMaxPlayers();
    @Value("#{target.password != null && !target.password.isEmpty()}")
    boolean isRequirePassword();

    interface RoomPlayerProjection {
        UserInfo getUser();
        interface UserInfo {
            Long getId();
        }
    }

    interface MatchProjection{
        Long getId();
        PuzzleProjection getPuzzle();
        MatchMode getMode();
    }
    interface UserProjection{
        Long getId();
        String getUsername();
    }
    interface PuzzleProjection {
        String getOriginalImageUrl();
        Integer getNumberOfPieces();
        Integer getRowCount();
        Integer getColCount();
    }
}