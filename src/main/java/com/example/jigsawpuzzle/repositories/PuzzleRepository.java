package com.example.jigsawpuzzle.repositories;

import com.example.jigsawpuzzle.domain.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PuzzleRepository extends JpaRepository<Puzzle,Long> {
    Optional<Puzzle> findPuzzleById(Long matchId);
}
