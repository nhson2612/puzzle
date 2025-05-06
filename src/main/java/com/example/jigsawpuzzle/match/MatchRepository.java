package com.example.jigsawpuzzle.match;

import com.example.jigsawpuzzle.domain.Match;
import com.example.jigsawpuzzle.domain.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match,Long> {
    @Query("SELECT m.status FROM Match m WHERE m.id = :matchId")
    MatchStatus findMatchStatusById(@Param("matchId") Long matchId);
}
