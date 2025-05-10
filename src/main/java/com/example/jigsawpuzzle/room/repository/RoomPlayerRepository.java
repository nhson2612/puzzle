package com.example.jigsawpuzzle.room.repository;

import com.example.jigsawpuzzle.domain.RoomPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomPlayerRepository extends JpaRepository<RoomPlayer,Long> {
    boolean existsByUser_Id(Long userId);
    Optional<RoomPlayer> findByUser_Id(Long userId);
}
