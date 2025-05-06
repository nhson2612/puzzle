package com.example.jigsawpuzzle.match;

import com.example.jigsawpuzzle.domain.Match;
import com.example.jigsawpuzzle.domain.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match,Long> {
    @Query("SELECT m.status FROM Match m WHERE m.id = :matchId")
    MatchStatus findMatchStatusById(@Param("matchId") Long matchId);

    @Query("SELECT m.id FROM Match m JOIN m.players p WHERE p.id = :userId AND m.status != 'FINISHED' AND m.status != 'NA'")
    List<Long> findActiveMatchesByUserId(@Param("userId") Long userId);

}