package com.example.jigsawpuzzle.repositories;

import com.example.jigsawpuzzle.domain.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession,Long> {
}
