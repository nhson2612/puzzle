package com.example.jigsawpuzzle.room.repository;

import com.example.jigsawpuzzle.domain.Room;
import com.example.jigsawpuzzle.domain.RoomStatus;
import com.example.jigsawpuzzle.room.repository.projection.RoomWithoutPassword;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
        @EntityGraph(attributePaths = {
                "host",
                "roomPlayers",
                "currentMatch",
                "currentMatch.puzzle"
        })
        List<RoomWithoutPassword> findAllRoomsProjectedBy();
        @Modifying
        @Query("UPDATE Room r SET r.status = :status WHERE r.id = :roomId")
        int updateRoomStatusById(@Param("roomId") Long roomId, @Param("status") RoomStatus status);
        @Query("SELECT COUNT(rp) FROM RoomPlayer rp WHERE rp.room.id = :roomId")
        int countPlayersInRoom(@Param("roomId") Long roomId);
        @Query("""
        SELECT r FROM Room r
        LEFT JOIN FETCH r.currentMatch cm
        LEFT JOIN FETCH r.roomPlayers rp
        LEFT JOIN FETCH rp.user
        WHERE r.id = :roomId
    """)
        Optional<Room> findByIdWithCurrentMatchAndPlayers(@Param("roomId") Long roomId);
        @Query("SELECT COUNT(rp) FROM RoomPlayer rp WHERE rp.room.id = :roomId AND rp.isReady = true")
        int countReadyPlayersInRoom(@Param("roomId") Long roomId);
        @Query("SELECT r FROM Room r LEFT JOIN FETCH r.roomPlayers WHERE r.id = :roomId")
        Optional<Room> findByIdWithPlayers(@Param("roomId") Long roomId);
        @Query("""
            SELECT r FROM Room r
            LEFT JOIN FETCH r.host h
            LEFT JOIN FETCH r.currentMatch m
            LEFT JOIN FETCH m.puzzle p
            LEFT JOIN FETCH r.roomPlayers rp
            LEFT JOIN FETCH rp.user u
            WHERE r.id = :roomId
        """)
        Optional<Room> findFullRoomById(@Param("roomId") Long roomId);

}
